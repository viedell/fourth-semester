import cv2
import sys
import os
sys.path.insert(0, os.path.dirname(__file__))
from face_detector import HaarFaceDetector, DNNFaceDetector, draw_detections

# ── Choose detector ──────────────────────
# Switch to HaarFaceDetector() if DNN is slow on your machine
MODEL_DIR = os.path.join(os.path.dirname(__file__), "models")
detector  = DNNFaceDetector(model_dir=MODEL_DIR, confidence_threshold=0.5)

cap = cv2.VideoCapture(0)  # 0 = default webcam
if not cap.isOpened():
    print("Error: Could not open webcam.")
    sys.exit(1)

print(f"Running face detection with {detector.name}")
print("Press Q to quit, S to save a screenshot")

frame_count = 0
while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Run detection every frame
    detections = detector.detect(frame)
    output     = draw_detections(frame, detections, color=(0, 120, 255))

    # HUD overlay
    cv2.putText(output, f"{detector.name} | Faces: {len(detections)}",
                (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 200), 2)
    cv2.putText(output, "Q: quit  |  S: screenshot",
                (10, 60), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (180, 180, 180), 1)

    cv2.imshow("Face Detection - Live", output)

    key = cv2.waitKey(1) & 0xFF
    if key == ord("q"):
        break
    elif key == ord("s"):
        fname = f"screenshot_{frame_count}.jpg"
        cv2.imwrite(fname, output)
        print(f"Saved: {fname}")
        frame_count += 1

cap.release()
cv2.destroyAllWindows()