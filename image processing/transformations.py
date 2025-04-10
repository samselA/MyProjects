import numpy as np

def rotate_90_right(image):
    """ Obrót obrazu o 90 stopni w prawo """
    return np.rot90(image, k=-1) 

def rotate_90_left(image):
    """ Obrót obrazu o 90 stopni w lewo """
    return np.rot90(image, k=1)  

def mirror_horizontal(image):
    """ Odbicie lustrzane w poziomie (lewo <-> prawo) """
    return np.fliplr(image)  

