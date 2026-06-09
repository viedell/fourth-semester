
import cv2
import numpy as np
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import time
import os
import urllib.request

from face_detector import HaarFaceDetector, DNNFaceDetector, draw_detections

# ─── Paths ───────────────────────────────────
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
IMG_IN   = os.path.join(BASE_DIR, "test_faces.jpg")
OUT_DIR  = os.path.join(BASE_DIR, "output")

# ─── Download a richer test image (group photo) ──────────────
# We'll use the classic Lena image (already downloaded) + build a synthetic
# 2-face composite to showcase multi-face detection too.

def build_test_image():
    """Download lena.jpg and create a 2-face composite."""
    import urllib.request
    lena_path = os.path.join(BASE_DIR, "lena.jpg")
    if not os.path.exists(lena_path):
        print("Downloading test image...")
        urllib.request.urlretrieve(
            "https://raw.githubusercontent.com/opencv/opencv/master/samples/data/lena.jpg",
            lena_path
        )
    lena = cv2.imread(lena_path)
    if lena is None:
        raise RuntimeError(f"Could not load lena.jpg from {lena_path}")
    lena_mirror = cv2.flip(lena, 1)
    composite = np.hstack([lena, lena_mirror])
    cv2.imwrite(IMG_IN, composite)
    print(f"Test image saved: {IMG_IN}  ({composite.shape[1]}x{composite.shape[0]})")
    return composite


# ─── Main Demo ───────────────────────────────

def main():
    print("=" * 60)
    print("   FACE DETECTION DEMO")
    print("   OpenCV Haar Cascade  vs  DNN SSD (ResNet-10)")
    print("=" * 60)

    # 1. Prepare test image
    image = build_test_image()
    image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    # 2. Load detectors
    print("\n[Setup] Loading detectors...")
    haar = HaarFaceDetector(scale_factor=1.1, min_neighbors=5, min_size=(30, 30))
    dnn  = DNNFaceDetector(model_dir=os.path.join(BASE_DIR, "models"), confidence_threshold=0.5)
    print("  Detectors ready.\n")

    # 3. Run both detectors + time them (avg of 5 runs)
    N_RUNS = 5
    print(f"[Detection] Running {N_RUNS} iterations each for timing...\n")

    haar_times, dnn_times = [], []
    for _ in range(N_RUNS):
        t = time.perf_counter(); haar.detect(image); haar_times.append((time.perf_counter()-t)*1000)
        t = time.perf_counter(); dnn.detect(image);  dnn_times.append((time.perf_counter()-t)*1000)

    haar_dets = haar.detect(image)
    dnn_dets  = dnn.detect(image)

    haar_avg = sum(haar_times) / N_RUNS
    dnn_avg  = sum(dnn_times)  / N_RUNS

    print(f"  Haar Cascade : {len(haar_dets)} face(s)  |  avg {haar_avg:.1f} ms")
    for i, d in enumerate(haar_dets, 1):
        print(f"    Face {i}: bbox=({d.x},{d.y},{d.w},{d.h})")

    print(f"  DNN SSD      : {len(dnn_dets)} face(s)  |  avg {dnn_avg:.1f} ms")
    for i, d in enumerate(dnn_dets, 1):
        print(f"    Face {i}: bbox=({d.x},{d.y},{d.w},{d.h}), conf={d.confidence:.3f}")

    # 4. Draw results
    haar_img = draw_detections(image, haar_dets, color=(0, 200, 0),   label_prefix="Face", show_confidence=False)
    dnn_img  = draw_detections(image, dnn_dets,  color=(0, 100, 255), label_prefix="Face", show_confidence=True)

    haar_rgb = cv2.cvtColor(haar_img, cv2.COLOR_BGR2RGB)
    dnn_rgb  = cv2.cvtColor(dnn_img,  cv2.COLOR_BGR2RGB)

    # 5. Build comparison figure
    fig, axes = plt.subplots(1, 3, figsize=(18, 6))
    fig.patch.set_facecolor("#1a1a2e")
    for ax in axes:
        ax.set_facecolor("#16213e")
        for spine in ax.spines.values():
            spine.set_edgecolor("#e94560")

    # Original
    axes[0].imshow(image_rgb)
    axes[0].set_title("Original Image", color="white", fontsize=13, fontweight="bold", pad=10)
    axes[0].axis("off")

    # Haar result
    axes[1].imshow(haar_rgb)
    axes[1].set_title(
        f"Haar Cascade\n{len(haar_dets)} face(s) detected | {haar_avg:.1f} ms avg",
        color="#a8ff78", fontsize=12, fontweight="bold", pad=10
    )
    axes[1].axis("off")
    # Annotate boxes info
    for i, d in enumerate(haar_dets, 1):
        axes[1].annotate(
            f"F{i}", xy=(d.x + d.w/2, d.y + d.h/2),
            fontsize=18, color="#a8ff78", fontweight="bold",
            ha="center", va="center",
            bbox=dict(boxstyle="round,pad=0.2", fc="black", alpha=0.4)
        )

    # DNN result
    axes[2].imshow(dnn_rgb)
    axes[2].set_title(
        f"DNN SSD (ResNet-10)\n{len(dnn_dets)} face(s) detected | {dnn_avg:.1f} ms avg",
        color="#78b8ff", fontsize=12, fontweight="bold", pad=10
    )
    axes[2].axis("off")
    for i, d in enumerate(dnn_dets, 1):
        axes[2].annotate(
            f"{d.confidence:.2f}", xy=(d.x + d.w/2, d.y + d.h/2),
            fontsize=11, color="#78b8ff", fontweight="bold",
            ha="center", va="center",
            bbox=dict(boxstyle="round,pad=0.2", fc="black", alpha=0.4)
        )

    fig.suptitle(
        "Face Detection: Haar Cascade vs DNN SSD",
        color="white", fontsize=15, fontweight="bold", y=1.01
    )

    legend_patches = [
        mpatches.Patch(color="#a8ff78", label=f"Haar Cascade: {len(haar_dets)} face(s)"),
        mpatches.Patch(color="#78b8ff", label=f"DNN SSD: {len(dnn_dets)} face(s)"),
    ]
    fig.legend(handles=legend_patches, loc="lower center", ncol=2,
               facecolor="#1a1a2e", edgecolor="#e94560",
               labelcolor="white", fontsize=11, bbox_to_anchor=(0.5, -0.04))

    plt.tight_layout()
    comparison_path = os.path.join(OUT_DIR, "comparison.png")
    plt.savefig(comparison_path, dpi=150, bbox_inches="tight",
                facecolor=fig.get_facecolor())
    plt.close()
    print(f"\n[Output] Comparison image saved: {comparison_path}")

    # 6. Performance bar chart
    fig2, ax = plt.subplots(figsize=(7, 4))
    fig2.patch.set_facecolor("#1a1a2e")
    ax.set_facecolor("#16213e")

    bars = ax.bar(
        ["Haar Cascade", "DNN SSD\n(ResNet-10)"],
        [haar_avg, dnn_avg],
        color=["#a8ff78", "#78b8ff"],
        width=0.5,
        edgecolor="#e94560",
        linewidth=1.2,
    )
    for bar, val in zip(bars, [haar_avg, dnn_avg]):
        ax.text(
            bar.get_x() + bar.get_width() / 2,
            bar.get_height() + 0.3,
            f"{val:.1f} ms",
            ha="center", va="bottom", color="white", fontsize=12, fontweight="bold"
        )

    ax.set_ylabel("Avg Inference Time (ms)", color="white", fontsize=11)
    ax.set_title(f"Inference Speed Comparison ({N_RUNS} runs, CPU)", color="white", fontsize=12, fontweight="bold")
    ax.tick_params(colors="white")
    for spine in ax.spines.values():
        spine.set_edgecolor("#444")
    ax.set_ylim(0, max(haar_avg, dnn_avg) * 1.3)

    perf_path = os.path.join(OUT_DIR, "performance.png")
    plt.tight_layout()
    plt.savefig(perf_path, dpi=150, bbox_inches="tight", facecolor=fig2.get_facecolor())
    plt.close()
    print(f"[Output] Performance chart saved: {perf_path}")

    # 7. Summary table
    print("\n" + "=" * 60)
    print("   RESULTS SUMMARY")
    print("=" * 60)
    print(f"{'Metric':<30} {'Haar Cascade':>15} {'DNN SSD':>15}")
    print("-" * 60)
    print(f"{'Faces Detected':<30} {len(haar_dets):>15} {len(dnn_dets):>15}")
    print(f"{'Avg Inference (ms)':<30} {haar_avg:>15.1f} {dnn_avg:>15.1f}")
    print(f"{'Requires GPU':<30} {'No':>15} {'No':>15}")
    print(f"{'Model Size':<30} {'~1 KB XML':>15} {'~2.7 MB':>15}")
    print(f"{'Provides Confidence':<30} {'No':>15} {'Yes':>15}")
    print("=" * 60)

    return comparison_path, perf_path


if __name__ == "__main__":
    main()
