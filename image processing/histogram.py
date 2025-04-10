import numpy as np
from matplotlib import pyplot as plt
from PyQt5.QtGui import QImage, QPixmap
from matplotlib.backends.backend_agg import FigureCanvasAgg as FigureCanvas
import pixel_ops


def histogram(image):
    image_grayscale = pixel_ops.grayscale(image)
    pixels = image_grayscale.flatten()

    fig, ax = plt.subplots(figsize=(3, 3), dpi=100)
    ax.hist(pixels, bins=256, range=(0, 255), color='gray', alpha=0.7)
    ax.set_title("Histogram")
    ax.set_xlabel("Poziom jasności")
    ax.set_ylabel("Liczba pikseli")

    fig.tight_layout()

    return figure_to_qimage(fig)


def histogram_rgb(image):
    if len(image.shape) != 3 or image.shape[2] != 3:
        return histogram(image)

    b_channel = image[:, :, 0]  
    g_channel = image[:, :, 1] 
    r_channel = image[:, :, 2] 

    fig, ax = plt.subplots(figsize=(6, 4), dpi=100)

    ax.hist(r_channel.flatten(), bins=256, range=(0, 256), color='red', alpha=0.7)
    ax.hist(g_channel.flatten(), bins=256, range=(0, 256), color='green', alpha=0.7)
    ax.hist(b_channel.flatten(), bins=256, range=(0, 256), color='blue', alpha=0.7)

    ax.set_title("Histogram RGB")
    ax.set_xlabel("Poziom jasności")
    ax.set_ylabel("Liczba pikseli")

    fig.tight_layout()

    return figure_to_qimage(fig)

def horizontal_projection(image):

    image_binary = pixel_ops.binarization(image)
    image_binary[image_binary == 0]   = 1
    image_binary[image_binary == 255] = 0
  
    projection = np.sum(image_binary, axis = 1)

    fig, ax = plt.subplots(figsize=(4, 2), dpi=100)
    ax.barh(range(len(projection)), projection, color='black', height=1) 
    ax.set_title("Projekcja pozioma")

    ax.set_xlim(0, np.max(projection)) 
    ax.set_ylim(0, image.shape[0])

    return figure_to_qimage(fig)

def vertical_projection(image):

    image_binary = pixel_ops.binarization(image)
    image_binary[image_binary == 0]   = 1

    image_binary[image_binary == 255] = 0
  
    projection = np.sum(image_binary, axis = 0)

    fig, ax = plt.subplots(figsize=(4, 2), dpi=100)
    ax.bar(range(len(projection)), projection, color='black', width = 1)
    ax.set_title("Projekcja pionowa")
    ax.set_ylim(0, np.max(projection)) 
    ax.set_xlim(0, image.shape[1])

    return figure_to_qimage(fig)


def figure_to_qimage(fig):

    canvas = FigureCanvas(fig)
    canvas.draw()
    width, height = canvas.get_width_height()
    img = np.frombuffer(canvas.buffer_rgba(), dtype=np.uint8).reshape(height, width, 4)
    qimg = QImage(img, width, height, QImage.Format_RGBA8888)
    return qimg


