
import cv2
import numpy as np
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch
import time
import os
import sys

sys.path.insert(0, "/home/claude/face_detection")
from face_detector import HaarFaceDetector, DNNFaceDetector, draw_detections

# ── Colors ──────────────────────────────────
BG_DARK    = "#0f0f23"
BG_PANEL   = "#1a1a35"
HAAR_COLOR = "#a8ff78"
DNN_COLOR  = "#78b8ff"
ACCENT     = "#e94560"
TEXT_COLOR = "white"

BASE_DIR  = os.path.dirname(os.path.abspath(__file__))
MODEL_DIR = os.path.join(BASE_DIR, "models")
IMG_PATH  = os.path.join(BASE_DIR, "test_faces.jpg")
OUT_PATH  = os.path.join(BASE_DIR, "output", "analysis_report.png")

def run():
    image = cv2.imread(IMG_PATH)
    image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    haar = HaarFaceDetector(scale_factor=1.1, min_neighbors=5)
    dnn  = DNNFaceDetector(model_dir=MODEL_DIR, confidence_threshold=0.5)

    # Timing (10 runs)
    N = 10
    haar_times = []
    dnn_times  = []
    for _ in range(N):
        t = time.perf_counter(); haar.detect(image); haar_times.append((time.perf_counter()-t)*1000)
        t = time.perf_counter(); dnn.detect(image);  dnn_times.append((time.perf_counter()-t)*1000)

    haar_dets = haar.detect(image)
    dnn_dets  = dnn.detect(image)

    haar_img_bgr = draw_detections(image, haar_dets, color=(0, 200, 0),   label_prefix="Face", show_confidence=False)
    dnn_img_bgr  = draw_detections(image, dnn_dets,  color=(0, 120, 255), label_prefix="Face", show_confidence=True)
    haar_rgb = cv2.cvtColor(haar_img_bgr, cv2.COLOR_BGR2RGB)
    dnn_rgb  = cv2.cvtColor(dnn_img_bgr,  cv2.COLOR_BGR2RGB)

    # ── Figure layout ──────────────────────────
    fig = plt.figure(figsize=(20, 14), facecolor=BG_DARK)
    gs = gridspec.GridSpec(
        3, 3,
        figure=fig,
        hspace=0.45,
        wspace=0.35,
        left=0.05, right=0.95,
        top=0.92, bottom=0.06,
    )

    # Title
    fig.text(
        0.5, 0.96,
        "Face Detection Analysis Report — OpenCV Haar vs DNN SSD",
        ha="center", va="center", color=TEXT_COLOR,
        fontsize=18, fontweight="bold"
    )
    fig.text(
        0.5, 0.935,
        "Practical Exercise  |  Computer Vision",
        ha="center", va="center", color="#aaaacc", fontsize=11
    )

    def style_ax(ax, title="", title_color=TEXT_COLOR):
        ax.set_facecolor(BG_PANEL)
        for spine in ax.spines.values():
            spine.set_edgecolor("#333355")
            spine.set_linewidth(1.2)
        ax.tick_params(colors="#aaaacc", labelsize=9)
        if title:
            ax.set_title(title, color=title_color, fontsize=11, fontweight="bold", pad=8)
        return ax

    # ── Row 0: Images ─────────────────────────
    ax0 = fig.add_subplot(gs[0, 0])
    ax0.imshow(image_rgb)
    ax0.axis("off")
    ax0.set_title("Original Image", color=TEXT_COLOR, fontsize=11, fontweight="bold", pad=8)
    ax0.set_facecolor(BG_PANEL)

    ax1 = fig.add_subplot(gs[0, 1])
    ax1.imshow(haar_rgb)
    ax1.axis("off")
    ax1.set_title(
        f"Haar Cascade  →  {len(haar_dets)} face(s)",
        color=HAAR_COLOR, fontsize=11, fontweight="bold", pad=8
    )
    ax1.set_facecolor(BG_PANEL)

    ax2 = fig.add_subplot(gs[0, 2])
    ax2.imshow(dnn_rgb)
    ax2.axis("off")
    ax2.set_title(
        f"DNN SSD ResNet-10  →  {len(dnn_dets)} face(s)",
        color=DNN_COLOR, fontsize=11, fontweight="bold", pad=8
    )
    ax2.set_facecolor(BG_PANEL)

    # ── Row 1 left: Speed boxplot ─────────────
    ax3 = fig.add_subplot(gs[1, 0])
    style_ax(ax3, "Inference Time Distribution (10 runs, CPU)")
    bp = ax3.boxplot(
        [haar_times, dnn_times],
        labels=["Haar\nCascade", "DNN SSD"],
        patch_artist=True,
        widths=0.5,
        medianprops=dict(color=ACCENT, linewidth=2),
        whiskerprops=dict(color="#aaaacc"),
        capprops=dict(color="#aaaacc"),
        flierprops=dict(marker="o", color="#aaaacc", markersize=4),
    )
    bp["boxes"][0].set_facecolor(HAAR_COLOR + "66")
    bp["boxes"][0].set_edgecolor(HAAR_COLOR)
    bp["boxes"][1].set_facecolor(DNN_COLOR + "66")
    bp["boxes"][1].set_edgecolor(DNN_COLOR)
    ax3.set_ylabel("Time (ms)", color="#aaaacc", fontsize=9)
    ax3.yaxis.label.set_color("#aaaacc")

    # ── Row 1 mid: Confidence scores ─────────
    ax4 = fig.add_subplot(gs[1, 1])
    style_ax(ax4, "DNN Confidence Scores per Detected Face")
    if dnn_dets:
        labels = [f"Face {i}" for i in range(1, len(dnn_dets)+1)]
        confs  = [d.confidence for d in dnn_dets]
        bars   = ax4.bar(labels, confs, color=DNN_COLOR, edgecolor=ACCENT, linewidth=1.2)
        ax4.axhline(0.5, color=ACCENT, linestyle="--", linewidth=1, label="Threshold (0.5)")
        ax4.set_ylim(0, 1.1)
        ax4.set_ylabel("Confidence", color="#aaaacc", fontsize=9)
        for bar, c in zip(bars, confs):
            ax4.text(
                bar.get_x() + bar.get_width()/2,
                bar.get_height() + 0.02,
                f"{c:.3f}",
                ha="center", va="bottom", color=DNN_COLOR,
                fontsize=10, fontweight="bold"
            )
        ax4.legend(facecolor=BG_PANEL, edgecolor="#555", labelcolor="#aaaacc", fontsize=8)
    else:
        ax4.text(0.5, 0.5, "No detections", transform=ax4.transAxes,
                 ha="center", va="center", color="#aaaacc")

    # ── Row 1 right: BBox areas ───────────────
    ax5 = fig.add_subplot(gs[1, 2])
    style_ax(ax5, "Detected Face Bounding Box Areas")
    if haar_dets or dnn_dets:
        haar_areas = [d.area for d in haar_dets]
        dnn_areas  = [d.area for d in dnn_dets]
        x = np.arange(max(len(haar_areas), len(dnn_areas)))
        w = 0.35

        if haar_areas:
            ax5.bar(np.arange(len(haar_areas)) - w/2, haar_areas, w,
                    label="Haar", color=HAAR_COLOR, alpha=0.85, edgecolor="black")
        if dnn_areas:
            ax5.bar(np.arange(len(dnn_areas)) + w/2, dnn_areas, w,
                    label="DNN", color=DNN_COLOR, alpha=0.85, edgecolor="black")

        ax5.set_xlabel("Face Index (0-based)", color="#aaaacc", fontsize=9)
        ax5.set_ylabel("Area (px²)", color="#aaaacc", fontsize=9)
        ax5.legend(facecolor=BG_PANEL, edgecolor="#555", labelcolor="#aaaacc", fontsize=8)

    # ── Row 2: Comparison table ───────────────
    ax6 = fig.add_subplot(gs[2, :])
    ax6.set_facecolor(BG_PANEL)
    ax6.axis("off")
    ax6.set_title("Detector Comparison Summary", color=TEXT_COLOR, fontsize=12, fontweight="bold", pad=8)

    haar_avg = sum(haar_times) / N
    dnn_avg  = sum(dnn_times) / N

    rows = [
        ["Faces Detected",          str(len(haar_dets)),         str(len(dnn_dets))],
        ["Avg Inference Time",       f"{haar_avg:.1f} ms",        f"{dnn_avg:.1f} ms"],
        ["Provides Confidence Score","✗  No",                     "✓  Yes"],
        ["Model File Size",          "~1 KB (XML)",               "~2.7 MB (.caffemodel)"],
        ["Requires GPU",             "✗  No",                     "✗  No (CPU ok)"],
        ["Best For",                 "Speed, simple use cases",   "Accuracy, complex scenes"],
        ["False Positive Risk",      "Medium–High",               "Low–Medium"],
    ]
    col_labels = ["Property", "Haar Cascade", "DNN SSD (ResNet-10)"]

    table = ax6.table(
        cellText=rows,
        colLabels=col_labels,
        loc="center",
        cellLoc="center",
    )
    table.auto_set_font_size(False)
    table.set_fontsize(10)
    table.scale(1, 1.8)

    for (r, c), cell in table.get_celld().items():
        cell.set_facecolor(BG_PANEL)
        cell.set_edgecolor("#333355")
        if r == 0:
            cell.set_facecolor("#2a2a55")
            cell.set_text_props(color=TEXT_COLOR, fontweight="bold")
        elif c == 1:
            cell.set_text_props(color=HAAR_COLOR)
        elif c == 2:
            cell.set_text_props(color=DNN_COLOR)
        else:
            cell.set_text_props(color="#ccccdd")

    plt.savefig(OUT_PATH, dpi=130, bbox_inches="tight", facecolor=BG_DARK)
    plt.close()
    print(f"Analysis report saved: {OUT_PATH}")
    return OUT_PATH


if __name__ == "__main__":
    run()
