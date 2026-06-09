"""
    python detect.py --image path/to/image.jpg
    python detect.py --image path/to/image.jpg --detector dnn --threshold 0.6
    python detect.py --image path/to/image.jpg --detector haar --output result.jpg
"""

import argparse
import cv2
import os
import sys

# make sure face_detector module is importable from same dir
sys.path.insert(0, os.path.dirname(__file__))
from face_detector import HaarFaceDetector, DNNFaceDetector, draw_detections, run_detection


def main():
    parser = argparse.ArgumentParser(description="Face Detection with OpenCV")
    parser.add_argument("--image",     required=True,         help="Path to input image")
    parser.add_argument("--detector",  default="both",        choices=["haar", "dnn", "both"],
                        help="Which detector to use (default: both)")
    parser.add_argument("--threshold", type=float, default=0.5,
                        help="DNN confidence threshold (default: 0.5)")
    parser.add_argument("--output",    default=None,
                        help="Output image path (optional)")
    parser.add_argument("--scale",     type=float, default=1.1,
                        help="Haar scale factor (default: 1.1)")
    parser.add_argument("--neighbors", type=int,   default=5,
                        help="Haar min neighbors (default: 5)")
    args = parser.parse_args()

    if not os.path.exists(args.image):
        print(f"[ERROR] File not found: {args.image}")
        sys.exit(1)

    image = cv2.imread(args.image)
    if image is None:
        print(f"[ERROR] Could not read image: {args.image}")
        sys.exit(1)

    print(f"Image: {args.image}  ({image.shape[1]}x{image.shape[0]})")
    print()

    model_dir = os.path.join(os.path.dirname(__file__), "models")

    if args.detector in ("haar", "both"):
        haar = HaarFaceDetector(scale_factor=args.scale, min_neighbors=args.neighbors)
        dets, ms = run_detection(args.image, haar, verbose=True)
        if args.output and args.detector == "haar":
            result = draw_detections(image, dets, color=(0, 200, 0))
            cv2.imwrite(args.output, result)
            print(f"\nOutput saved: {args.output}")

    if args.detector in ("dnn", "both"):
        dnn = DNNFaceDetector(model_dir=model_dir, confidence_threshold=args.threshold)
        dets, ms = run_detection(args.image, dnn, verbose=True)
        if args.output and args.detector == "dnn":
            result = draw_detections(image, dets, color=(0, 100, 255))
            cv2.imwrite(args.output, result)
            print(f"\nOutput saved: {args.output}")

    if args.detector == "both" and args.output:
        # Save DNN result by default
        result = draw_detections(image, dets, color=(0, 100, 255))
        cv2.imwrite(args.output, result)
        print(f"\nOutput saved: {args.output}")


if __name__ == "__main__":
    main()
