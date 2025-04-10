import numpy as np
from numpy.lib._stride_tricks_impl import as_strided


def _apply_kernel(image, kernel, mode='reflect'):

    kernel = np.array(kernel, dtype=np.float32)
    kh, kw = kernel.shape
    pad_h, pad_w = kh // 2, kw // 2

    if image.ndim == 2:
        # Grayscale image
        padded = np.pad(image, ((pad_h, pad_h), (pad_w, pad_w)), mode=mode)
        H, W = image.shape
        shape = (H, W, kh, kw)
        strides = padded.strides * 2
        windows = as_strided(padded, shape=shape, strides=strides)
        result = np.einsum('ijkl,kl->ij', windows, kernel)
        return result.clip(0, 255).astype(np.uint8)
    elif image.ndim == 3:
        # Color image
        H, W, C = image.shape
        padded = np.pad(image, ((pad_h, pad_h), (pad_w, pad_w), (0, 0)), mode=mode)
        output = np.empty((H, W, C), dtype=np.float32)
        for c in range(C):
            channel = padded[:, :, c]
            shape = (H, W, kh, kw)
            strides = channel.strides * 2
            windows = as_strided(channel, shape=shape, strides=strides)
            result = np.einsum('ijkl,kl->ij', windows, kernel)
            output[:, :, c] = result
        return output.clip(0, 255).astype(np.uint8)
    else:
        raise ValueError("Unsupported image shape.")


def averaging_filter(image, kernel_size=3):

    kernel = np.ones((kernel_size, kernel_size), dtype=np.float32) / (kernel_size ** 2)
    return _apply_kernel(image, kernel, mode='reflect')


def sharpening_filter(image, intensity):
    kernel = np.array([[0, -1, 0],
                       [-1, intensity, -1],
                       [0, -1, 0]], dtype=np.float32)
    return _apply_kernel(image, kernel, mode='reflect')


def gaussian_blur(image, kernel_size=5, sigma=1.0):

    ax = np.arange(-kernel_size // 2 + 1, kernel_size // 2 + 1)
    xx, yy = np.meshgrid(ax, ax)
    kernel = np.exp(-(xx ** 2 + yy ** 2) / (2 * sigma ** 2))
    kernel /= kernel.sum()
    return _apply_kernel(image, kernel, mode='reflect')


def color_quantization(image, levels=4):

    image = image.astype(np.uint8)
    step = 256 // levels
    quantized = (image // step) * step

    return quantized.astype(np.uint8)

def roberts_cross(image):
    roberts_x = np.array([[1, 0], [0, -1]], dtype=np.float32)
    roberts_y = np.array([[0, 1], [-1, 0]], dtype=np.float32)

    gx = _apply_kernel(image, roberts_x, mode='reflect')
    gy = _apply_kernel(image, roberts_y, mode='reflect')

    gradient_magnitude = np.sqrt(gx.astype(np.float32) ** 2 + gy.astype(np.float32) ** 2)
    return np.clip(gradient_magnitude, 0, 255).astype(np.uint8)

def sobel_operator(image):
    sobel_x = np.array([[-1, 0, 1], 
                         [-2, 0, 2], 
                         [-1, 0, 1]], dtype=np.float32)

    sobel_y = np.array([[-1, -2, -1], 
                         [0,  0,  0], 
                         [1,  2,  1]], dtype=np.float32)

    gx = _apply_kernel(image, sobel_x, mode='reflect')
    gy = _apply_kernel(image, sobel_y, mode='reflect')

    gradient_magnitude = np.sqrt(gx.astype(np.float32) ** 2 + gy.astype(np.float32) ** 2)
    
    return np.clip(gradient_magnitude, 0, 255).astype(np.uint8)
def prewitt_operator(image):

    prewitt_x = np.array([[-1, 0, 1], 
                           [-1, 0, 1], 
                           [-1, 0, 1]], dtype=np.float32)

    prewitt_y = np.array([[-1, -1, -1], 
                           [ 0,  0,  0], 
                           [ 1,  1,  1]], dtype=np.float32)

    gx = _apply_kernel(image, prewitt_x, mode='reflect')
    gy = _apply_kernel(image, prewitt_y, mode='reflect')

    gradient_magnitude = np.sqrt(gx.astype(np.float32) ** 2 + gy.astype(np.float32) ** 2)
    

    return np.clip(gradient_magnitude, 0, 255).astype(np.uint8)