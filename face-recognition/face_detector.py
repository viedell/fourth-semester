
import cv2
import numpy as np
import os
import time
from dataclasses import dataclass
from typing import List, Tuple, Optional


# ─────────────────────────────────────────────
# Data structures
# ─────────────────────────────────────────────

@dataclass
class DetectionResult:
    x: int
    y: int
    w: int
    h: int
    confidence: float  # 1.0 for Haar (no score), 0-1 for DNN

    @property
    def bbox(self) -> Tuple[int, int, int, int]:
        return (self.x, self.y, self.w, self.h)

    @property
    def area(self) -> int:
        return self.w * self.h


# ─────────────────────────────────────────────
# Haar Cascade Detector
# ─────────────────────────────────────────────

class HaarFaceDetector:
    """
    Classic Viola-Jones face detector using pre-trained Haar Cascades.
    Fast, lightweight, works without GPU.
    """

    def __init__(
        self,
        cascade_path: Optional[str] = None,
        scale_factor: float = 1.1,
        min_neighbors: int = 5,
        min_size: Tuple[int, int] = (30, 30),
    ):
        if cascade_path is None:
            cascade_path = cv2.data.haarcascades + "haarcascade_frontalface_default.xml"
        self.classifier = cv2.CascadeClassifier(cascade_path)
        if self.classifier.empty():
            raise RuntimeError(f"Failed to load cascade from: {cascade_path}")
        self.scale_factor = scale_factor
        self.min_neighbors = min_neighbors
        self.min_size = min_size

    def detect(self, image: np.ndarray) -> List[DetectionResult]:
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        gray = cv2.equalizeHist(gray)  # improve contrast
        faces = self.classifier.detectMultiScale(
            gray,
            scaleFactor=self.scale_factor,
            minNeighbors=self.min_neighbors,
            minSize=self.min_size,
            flags=cv2.CASCADE_SCALE_IMAGE,
        )
        results = []
        if len(faces) > 0:
            for (x, y, w, h) in faces:
                results.append(DetectionResult(x=x, y=y, w=w, h=h, confidence=1.0))
        return results

    @property
    def name(self) -> str:
        return "Haar Cascade"


# ─────────────────────────────────────────────
# DNN / SSD Detector
# ─────────────────────────────────────────────

class DNNFaceDetector:
    """
    Deep learning face detector using OpenCV's DNN module with
    a pre-trained SSD (Single Shot MultiBox Detector) + ResNet-10 backbone.
    Better accuracy than Haar, especially for rotated/occluded faces.
    """

    MODEL_URL_CFG = (
        "https://raw.githubusercontent.com/opencv/opencv/master/"
        "samples/dnn/face_detector/deploy.prototxt"
    )
    MODEL_URL_WEIGHTS = (
        "https://raw.githubusercontent.com/opencv/opencv_3rdparty/"
        "dnn_samples_face_detector_20170830/res10_300x300_ssd_iter_140000.caffemodel"
    )

    def __init__(
        self,
        model_dir: str = "/home/claude/face_detection/models",
        confidence_threshold: float = 0.5,
    ):
        self.confidence_threshold = confidence_threshold
        self.cfg_path = os.path.join(model_dir, "deploy.prototxt")
        self.weights_path = os.path.join(model_dir, "res10_300x300_ssd_iter_140000.caffemodel")
        os.makedirs(model_dir, exist_ok=True)
        self._download_if_needed()
        self.net = cv2.dnn.readNetFromCaffe(self.cfg_path, self.weights_path)

    def _download_if_needed(self):
        import urllib.request
        if not os.path.exists(self.cfg_path):
            print("  Downloading DNN config...")
            urllib.request.urlretrieve(self.MODEL_URL_CFG, self.cfg_path)
        if not os.path.exists(self.weights_path):
            print("  Downloading DNN weights (~2.7 MB)...")
            urllib.request.urlretrieve(self.MODEL_URL_WEIGHTS, self.weights_path)

    def detect(self, image: np.ndarray) -> List[DetectionResult]:
        h, w = image.shape[:2]
        blob = cv2.dnn.blobFromImage(
            cv2.resize(image, (300, 300)),
            scalefactor=1.0,
            size=(300, 300),
            mean=(104.0, 177.0, 123.0),
            swapRB=False,
            crop=False,
        )
        self.net.setInput(blob)
        detections = self.net.forward()  # shape: (1, 1, N, 7)

        results = []
        for i in range(detections.shape[2]):
            confidence = float(detections[0, 0, i, 2])
            if confidence < self.confidence_threshold:
                continue
            x1 = int(detections[0, 0, i, 3] * w)
            y1 = int(detections[0, 0, i, 4] * h)
            x2 = int(detections[0, 0, i, 5] * w)
            y2 = int(detections[0, 0, i, 6] * h)
            # clamp to image bounds
            x1, y1 = max(0, x1), max(0, y1)
            x2, y2 = min(w, x2), min(h, y2)
            results.append(DetectionResult(
                x=x1, y=y1, w=x2 - x1, h=y2 - y1, confidence=confidence
            ))
        return results

    @property
    def name(self) -> str:
        return "DNN SSD (ResNet-10)"


# ─────────────────────────────────────────────
# Drawing utility
# ─────────────────────────────────────────────

def draw_detections(
    image: np.ndarray,
    detections: List[DetectionResult],
    color: Tuple[int, int, int] = (0, 255, 0),
    label_prefix: str = "Face",
    show_confidence: bool = True,
) -> np.ndarray:
    output = image.copy()
    for i, det in enumerate(detections, 1):
        x, y, w, h = det.x, det.y, det.w, det.h
        cv2.rectangle(output, (x, y), (x + w, y + h), color, 2)

        label = f"{label_prefix} {i}"
        if show_confidence and det.confidence < 1.0:
            label += f" ({det.confidence:.2f})"

        # Background for label
        (tw, th), _ = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.55, 1)
        cv2.rectangle(output, (x, y - th - 8), (x + tw + 4, y), color, -1)
        cv2.putText(
            output, label, (x + 2, y - 4),
            cv2.FONT_HERSHEY_SIMPLEX, 0.55, (0, 0, 0), 1, cv2.LINE_AA
        )
    return output


# ─────────────────────────────────────────────
# Detection runner
# ─────────────────────────────────────────────

def run_detection(
    image_path: str,
    detector,
    output_path: Optional[str] = None,
    verbose: bool = True,
) -> Tuple[List[DetectionResult], float]:
    image = cv2.imread(image_path)
    if image is None:
        raise FileNotFoundError(f"Could not load image: {image_path}")

    t0 = time.perf_counter()
    detections = detector.detect(image)
    elapsed_ms = (time.perf_counter() - t0) * 1000

    if verbose:
        print(f"[{detector.name}] {len(detections)} face(s) detected in {elapsed_ms:.1f} ms")
        for i, d in enumerate(detections, 1):
            conf_str = f", conf={d.confidence:.2f}" if d.confidence < 1.0 else ""
            print(f"  Face {i}: x={d.x}, y={d.y}, w={d.w}, h={d.h}{conf_str}")

    if output_path:
        result_img = draw_detections(image, detections)
        cv2.imwrite(output_path, result_img)

    return detections, elapsed_ms
