import numpy as np

def grayscale(image):
    if image.ndim == 2:
        return image  # already grayscale
    weights = np.array([0.114, 0.587, 0.299])
    gray = np.dot(image[..., :3], weights)
    return gray.astype(np.uint8)

def negative(image):
    return 255 - image

def brightness(image, value):
    image_int = image.astype(np.int16)
    image_bright = image_int + value
    return np.clip(image_bright, 0, 255).astype(np.uint8)

def contrast(image, factor):
    mean = np.mean(image)
    adjusted = mean + factor * (image - mean)
    return np.clip(adjusted, 0, 255).astype(np.uint8)

def binarization(image, threshold=128):
    if image.ndim == 3 and image.shape[2] == 3:
        image = grayscale(image)
    return np.where(image >= threshold, 255, 0).astype(np.uint8)
