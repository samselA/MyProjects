import sys
import cv2
import numpy as np
from PyQt5.QtWidgets import (
    QApplication, QLabel, QMainWindow, QFileDialog, QVBoxLayout,
    QWidget, QHBoxLayout, QSlider, QAction, QSizePolicy, QPushButton, QDialog,
    QTextEdit, QDialogButtonBox
)
from PyQt5.QtCore import Qt
from PyQt5.QtGui import QPixmap, QImage, QIcon

import pixel_ops
import filters
import histogram
import transformations

class CustomKernelDialog(QDialog):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setWindowTitle("Custom Kernel")
        self.kernel = None

        layout = QVBoxLayout(self)

        info_label = QLabel("Wprowadź macierz filtrującą (wiersze oddzielone nową linią, wartości oddzielone przecinkami):", self)
        layout.addWidget(info_label)

        self.text_edit = QTextEdit(self)
        # Domyślna macierz – przykładowa macierz wyostrzająca
        self.text_edit.setPlainText("0, -1, 0\n-1, 5, -1\n0, -1, 0")
        layout.addWidget(self.text_edit)

        buttons = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel, self)
        buttons.accepted.connect(self.accept)
        buttons.rejected.connect(self.reject)
        layout.addWidget(buttons)

    def accept(self):
        text = self.text_edit.toPlainText()
        try:
            # Podziel tekst na wiersze i sparsuj wartości
            rows = text.strip().split('\n')
            kernel_list = []
            for row in rows:
                values = [float(v.strip()) for v in row.split(',')]
                kernel_list.append(values)
            self.kernel = np.array(kernel_list, dtype=np.float32)
            super().accept()
        except Exception as e:
            print("Błąd parsowania macierzy:", e)
            self.kernel = None
            super().reject()

class ImageProcessor(QMainWindow):
    def __init__(self):
        super().__init__()
        self.slider = None
        self.controls_layout = None
        self.processed_label = None
        self.original_label = None
        self.image_layout = None
        self.layout = None
        self.central_widget = None
        self.original_image = None
        self.processed_image = None  # „zatwierdzony” stan
        self.preview_image = None    # tymczasowy podgląd operacji

        self.current_analysis = None

        self.initUI()

    def initUI(self):
        self.setWindowTitle("Image Processing App")
        self.setGeometry(100, 100, 1000, 600)
        self.setWindowIcon(QIcon("picture.png"))

        self.central_widget = QWidget()
        self.setCentralWidget(self.central_widget)
        self.layout = QVBoxLayout()
        self.central_widget.setLayout(self.layout)

        self.image_layout = QHBoxLayout()
        self.layout.addLayout(self.image_layout)

        # Oryginalny obraz:
        orig_layout = QVBoxLayout()
        self.original_label = QLabel(self)
        self.original_label.setStyleSheet("border: 1px solid black;")
        self.original_label.setScaledContents(True)  # Enable scaling
        self.original_label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)  # Fixed size policy
        self.original_label.setFixedSize(500, 500)  # Fixed size
        self.original_label.setAlignment(Qt.AlignCenter)
        orig_layout.addWidget(self.original_label)
        orig_caption = QLabel("Original Image", self)
        orig_caption.setAlignment(Qt.AlignCenter)
        orig_layout.addWidget(orig_caption)
        self.image_layout.addLayout(orig_layout)

        # Przetworzony obraz:
        proc_layout = QVBoxLayout()
        self.processed_label = QLabel(self)
        self.processed_label.setStyleSheet("border: 1px solid black;")
        self.processed_label.setScaledContents(True)  # Enable scaling
        self.processed_label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)  # Fixed size policy
        self.processed_label.setFixedSize(500, 500)  # Fixed size
        self.processed_label.setAlignment(Qt.AlignCenter)
        proc_layout.addWidget(self.processed_label)
        proc_caption = QLabel("Processed Image", self)
        proc_caption.setAlignment(Qt.AlignCenter)
        proc_layout.addWidget(proc_caption)
        self.image_layout.addLayout(proc_layout)

        # Wykres (np. histogram):
        self.histogram_label = QLabel()
        self.histogram_label.setStyleSheet("border: 1px solid black;")
        self.histogram_label.setScaledContents(True)
        self.histogram_label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
        self.histogram_label.setFixedSize(0, 0)
        self.image_layout.addWidget(self.histogram_label)

        # Controls layout for brightness and contrast sliders
        self.controls_layout = QHBoxLayout()
        self.layout.addLayout(self.controls_layout)

        # Suwak jasności
        self.brightness_slider = QSlider(Qt.Horizontal)
        self.brightness_slider.setMinimum(-100)
        self.brightness_slider.setMaximum(100)
        self.brightness_slider.setValue(0)
        self.brightness_slider.setFixedSize(300, 30)
        self.brightness_slider.valueChanged.connect(self.adjustBrightness)
        self.brightness_slider.setVisible(False)
        self.controls_layout.addWidget(self.brightness_slider)

        # Suwak kontrastu
        self.contrast_slider = QSlider(Qt.Horizontal)
        self.contrast_slider.setMinimum(-100)
        self.contrast_slider.setMaximum(100)
        self.contrast_slider.setValue(0)
        self.contrast_slider.setFixedSize(300, 30)
        self.contrast_slider.valueChanged.connect(self.adjustContrast)
        self.contrast_slider.setVisible(False)
        self.controls_layout.addWidget(self.contrast_slider)

        # Przycisk „Zatwierdź zmiany”
        self.commit_button = QPushButton("Zatwierdź zmiany")
        self.commit_button.clicked.connect(self.commitChanges)
        self.layout.addWidget(self.commit_button)

        self.createMenuBar()

    def createMenuBar(self):
        menubar = self.menuBar()

        # ---- Menu: File ----
        file_menu = menubar.addMenu("File")

        open_action = QAction("Open Image", self)
        open_action.triggered.connect(self.openImage)
        file_menu.addAction(open_action)

        reset_action = QAction("Reset Image", self)
        reset_action.triggered.connect(self.resetImage)
        file_menu.addAction(reset_action)

        # ---- Menu: Save Image action ----
        save_action = QAction("Save Image", self)
        save_action.triggered.connect(self.saveImage)
        file_menu.addAction(save_action)

        # ---- Menu: Transformations ----
        transform_menu = menubar.addMenu("Transformations")

        rotate_right_action = QAction("Rotate Right (90°)", self)
        rotate_right_action.triggered.connect(self.rotateRight)
        transform_menu.addAction(rotate_right_action)

        rotate_left_action = QAction("Rotate Left (90°)", self)
        rotate_left_action.triggered.connect(self.rotateLeft)
        transform_menu.addAction(rotate_left_action)

        mirror_action = QAction("Mirror", self)
        mirror_action.triggered.connect(self.mirrorImage)
        transform_menu.addAction(mirror_action)

        # ---- Menu: Pixel Operations ----
        pixel_ops_menu = menubar.addMenu("Pixel Operations")

        grayscale_action = QAction("Grayscale", self)
        grayscale_action.triggered.connect(lambda: self.applyPixelOperationByName("grayscale"))
        pixel_ops_menu.addAction(grayscale_action)

        negative_action = QAction("Negative", self)
        negative_action.triggered.connect(lambda: self.applyPixelOperationByName("negative"))
        pixel_ops_menu.addAction(negative_action)

        brightness_action = QAction("Brightness", self)
        brightness_action.triggered.connect(self.toggleBrightnessSlider)
        pixel_ops_menu.addAction(brightness_action)

        contrast_action = QAction("Contrast", self)
        contrast_action.triggered.connect(self.toggleContrastSlider)
        pixel_ops_menu.addAction(contrast_action)

        binarization_action = QAction("Binarization", self)
        binarization_action.triggered.connect(lambda: self.applyPixelOperationByName("binarization"))
        pixel_ops_menu.addAction(binarization_action)

        # ---- Menu: Filters ----
        filters_menu = menubar.addMenu("Filters")

        sharpening_action = QAction("Sharpening filter", self)
        sharpening_action.triggered.connect(lambda: self.applyFilterByName("sharpening"))
        filters_menu.addAction(sharpening_action)

        averaging_action = QAction("Averaging filter", self)
        averaging_action.triggered.connect(lambda: self.applyFilterByName("averaging"))
        filters_menu.addAction(averaging_action)

        gaussian_action = QAction("Gaussian blur", self)
        gaussian_action.triggered.connect(lambda: self.applyFilterByName("gaussian"))
        filters_menu.addAction(gaussian_action)

        color_quantization_action = QAction("Color Quantization", self)
        color_quantization_action.triggered.connect(lambda: self.applyFilterByName("color_quantization"))
        filters_menu.addAction(color_quantization_action)

        roberts_cross_action = QAction("Roberts' Cross", self)
        roberts_cross_action.triggered.connect(lambda: self.applyFilterByName("roberts"))
        filters_menu.addAction(roberts_cross_action)

        sobel_action = QAction("Sobel Operation", self)
        sobel_action.triggered.connect(lambda: self.applyFilterByName("sobel"))
        filters_menu.addAction(sobel_action)

        prewitt_action = QAction("Prewitt Operation", self)
        prewitt_action.triggered.connect(lambda: self.applyFilterByName("prewitt"))
        filters_menu.addAction(prewitt_action)

        custom_kernel_action = QAction("Custom Kernel", self)
        custom_kernel_action.triggered.connect(self.applyCustomKernel)
        filters_menu.addAction(custom_kernel_action)

         # ---- Menu: Analysis ----
        analysis_menu = menubar.addMenu("Analysis")

        histogram_action = QAction("Histogram", self)
        histogram_action.triggered.connect(lambda: self.updateAnalysisView("histogram"))
        analysis_menu.addAction(histogram_action)

        histogram_rgb_action = QAction("Histogram RGB", self)
        histogram_rgb_action.triggered.connect(lambda: self.updateAnalysisView("histogram_rgb"))
        analysis_menu.addAction(histogram_rgb_action)

        horizontal_proj_action = QAction("Horizontal Projection", self)
        horizontal_proj_action.triggered.connect(lambda: self.updateAnalysisView("horizontal_projection"))
        analysis_menu.addAction(horizontal_proj_action)

        vertical_proj_action = QAction("Vertical Projection", self)
        vertical_proj_action.triggered.connect(lambda: self.updateAnalysisView("vertical_projection"))
        analysis_menu.addAction(vertical_proj_action)

    # ---------------------------------------------
    #                MENU ACTIONS
    # ---------------------------------------------

    def openImage(self):
        file_name, _ = QFileDialog.getOpenFileName(self, "Open Image", "", "Images (*.png *.xpm *.jpg *.jpeg)")
        if file_name:
            self.original_image = cv2.imread(file_name)
            # Na starcie processed_image jest kopią oryginału
            self.processed_image = self.original_image.copy()
            # Nie ma żadnego preview
            self.preview_image = None

            self.hideAllSliders()
            self.displayImage()  # Wyświetlamy

    def resetImage(self):
        if self.original_image is not None:
            self.processed_image = self.original_image.copy()
            self.preview_image = None
            self.hideAllSliders()
            self.displayImage()

    def saveImage(self):
        if self.processed_image is None:
            return
        file_name, _ = QFileDialog.getSaveFileName(self, "Save Image", "", "PNG (*.png);;JPEG (*.jpg *.jpeg);;All Files (*)")
        if file_name:
            cv2.imwrite(file_name, self.processed_image)

    # ---------------------------------------------
    #          PIXEL OPERATIONS / FILTERS
    # ---------------------------------------------

    def applyPixelOperationByName(self, operation_name):
        if self.processed_image is None:
            return

        self.hideAllSliders()

        # Bazą do podglądu jest zawsze bieżący processed_image (zatwierdzony)
        base = self.processed_image.copy()

        if operation_name == "grayscale":
            updated = pixel_ops.grayscale(base)
        elif operation_name == "negative":
            updated = pixel_ops.negative(base)
        elif operation_name == "binarization":
            updated = pixel_ops.binarization(base, threshold=128)
        else:
            updated = base

        # Zamiast zapisywać do processed_image, dajemy to do preview_image
        self.preview_image = updated
        self.updateAnalysisView(self.current_analysis)
        self.displayImage()  # Pokaż podgląd

    def applyFilterByName(self, filter_name):
        if self.processed_image is None:
            return

        self.hideAllSliders()
        base = self.processed_image.copy()

        if filter_name == "averaging":
            updated = filters.averaging_filter(base, kernel_size=3)
        elif filter_name == "sharpening":
            updated = filters.sharpening_filter(base, intensity=5)
        elif filter_name == "gaussian":
            updated = filters.gaussian_blur(base, kernel_size=5, sigma=1.0)
        elif filter_name == "color_quantization":
            updated = filters.color_quantization(base, levels=2)
        elif filter_name == "roberts":
            updated = filters.roberts_cross(base)
        elif filter_name == "sobel":
            updated = filters.sobel_operator(base)
        elif filter_name == "prewitt":
            updated = filters.prewitt_operator(base)
        else:
            updated = base

        self.preview_image = updated
        self.displayImage()

    def applyCustomKernel(self):
        if self.processed_image is None:
            return

        # Utwórz dialog do wprowadzania macierzy
        dialog = CustomKernelDialog(self)
        if dialog.exec_() == QDialog.Accepted and dialog.kernel is not None:
            # Użyj aktualnego zatwierdzonego obrazu jako bazy
            base = self.processed_image.copy()
            # Zastosuj filtr z wprowadzonymi wagami;
            # upewnij się, że funkcja _apply_kernel jest zaimportowana (np. z modułu pixel_ops)
            updated = filters._apply_kernel(base, dialog.kernel, mode='reflect')
            # Wynik zapisujemy jako preview (podgląd)
            self.preview_image = updated
            self.updateAnalysisView(self.current_analysis)
            self.displayImage()

    # ---------------------------------------------
    #         BRIGHTNESS / CONTRAST
    # ---------------------------------------------
    def toggleBrightnessSlider(self):
        if self.processed_image is None:
            return

        # Ukrywamy suwak kontrastu
        self.contrast_slider.setVisible(False)

        # Jeśli suwak jasności nie jest widoczny -> pokaż i ustaw preview
        if not self.brightness_slider.isVisible():
            self.brightness_slider.setValue(0)
            self.brightness_slider.setVisible(True)
            # Czyścimy ewentualny poprzedni preview
            self.preview_image = None
            self.displayImage()
        else:
            # Jeśli był widoczny, nic nie zmieniamy – zatwierdzenie i tak robi commitChanges()
            pass

    def adjustBrightness(self):
        if self.processed_image is None:
            return

        base = self.processed_image.copy()
        value = self.brightness_slider.value()
        updated = pixel_ops.brightness(base, value)

        self.preview_image = updated
        self.displayImage()

    def toggleContrastSlider(self):
        if self.processed_image is None:
            return

        self.brightness_slider.setVisible(False)

        if not self.contrast_slider.isVisible():
            self.contrast_slider.setValue(0)
            self.contrast_slider.setVisible(True)
            self.preview_image = None
            self.displayImage()
        else:
            pass

    def adjustContrast(self):
        if self.processed_image is None:
            return

        base = self.processed_image.copy()
        slider_value = self.contrast_slider.value()
        factor = 1 + (slider_value / 200.0)
        updated = pixel_ops.contrast(base, factor)

        self.preview_image = updated
        self.displayImage()

    # ---------------------------------------------
    #        COMMIT CHANGES BUTTON
    # ---------------------------------------------
    def commitChanges(self):
        """
        Kopiuje self.preview_image do self.processed_image,
        jeśli preview_image istnieje. Tym samym zatwierdza ostatnią operację.
        """
        if self.preview_image is not None:
            self.processed_image = self.preview_image.copy()
            self.preview_image = None
            self.hideAllSliders()
            self.displayImage()

    # ---------------------------------------------
    #        ANALYSIS, HISTOGRAM
    # ---------------------------------------------
    def updateAnalysisView(self, analysis_type):
        # Analiza zawsze na zatwierdzonym obrazie (processed_image)
        if self.processed_image is None:
            return

        self.current_analysis = analysis_type
        if analysis_type == "histogram":
            qimg_analysis = histogram.histogram(self.processed_image)
        elif analysis_type == "histogram_rgb":
            qimg_analysis = histogram.histogram_rgb(self.processed_image)
        elif analysis_type == "horizontal_projection":
            qimg_analysis = histogram.horizontal_projection(self.processed_image)
        elif analysis_type == "vertical_projection":
            qimg_analysis = histogram.vertical_projection(self.processed_image)
        else:
            return

        # Convert to QPixmap
        pixmap_analysis = QPixmap.fromImage(qimg_analysis)
        # Skalowanie do max 1/4 szerokości ekranu
        screen_width = QApplication.primaryScreen().size().width()
        max_display_width = screen_width // 4

        orig_w = pixmap_analysis.width()
        orig_h = pixmap_analysis.height()
        scale_factor = min(1.0, max_display_width / orig_w)
        new_w = int(orig_w * scale_factor)
        new_h = int(pixmap_analysis.height() * scale_factor)
        pixmap_analysis = pixmap_analysis.scaled(new_w, new_h, Qt.KeepAspectRatio, Qt.SmoothTransformation)
        self.histogram_label.setPixmap(pixmap_analysis)
        self.histogram_label.setFixedSize(new_w, new_h)

    def rotateRight(self):
        """Obrót obrazu o 90° w prawo"""
        if self.processed_image is None:
            return
        # Obrót wykonywany jest na kopii processed_image
        transformed = transformations.rotate_90_right(self.processed_image.copy())
        self.processed_image = transformed
        self.updateAnalysisView(self.current_analysis)
        self.displayImage()

    def rotateLeft(self):
        """Obrót obrazu o 90° w lewo"""
        if self.processed_image is None:
            return
        transformed = transformations.rotate_90_left(self.processed_image.copy())
        self.processed_image = transformed
        self.updateAnalysisView(self.current_analysis)
        self.displayImage()

    def mirrorImage(self):
        """Odbicie lustrzane poziome"""
        if self.processed_image is None:
            return
        transformed = transformations.mirror_horizontal(self.processed_image.copy())
        self.processed_image = transformed
        self.updateAnalysisView(self.current_analysis)
        self.displayImage()

    # ---------------------------------------------
    #             DISPLAY THE IMAGES
    # ---------------------------------------------

    def displayImage(self):
        """
        Jeśli mamy preview_image, to pokazujemy je w processed_label.
        W przeciwnym razie pokazujemy processed_image.
        Oryginalny obraz zawsze w original_label.
        """
        if self.original_image is None:
            return

        # Oryginal
        self.showImageInLabel(self.original_image, self.original_label)

        # Podgląd
        if self.preview_image is not None:
            self.showImageInLabel(self.preview_image, self.processed_label)
        else:
            if self.processed_image is not None:
                self.showImageInLabel(self.processed_image, self.processed_label)

    def showImageInLabel(self, cv_img, label):
        """Pomocnicza funkcja: konwertuje cv2 -> QPixmap, skaluje i ustawia w labelu."""
        screen_width = QApplication.primaryScreen().size().width()
        max_display_width = screen_width // 4

        rgb_img = cv2.cvtColor(cv_img, cv2.COLOR_BGR2RGB)
        h, w, ch = rgb_img.shape
        bytes_per_line = ch * w
        qimg = QImage(rgb_img.data, w, h, bytes_per_line, QImage.Format_RGB888)

        scale_factor = min(1.0, max_display_width / w)
        new_w = int(w * scale_factor)
        new_h = int(h * scale_factor)
        pixmap = QPixmap.fromImage(qimg).scaled(new_w, new_h, Qt.KeepAspectRatio, Qt.SmoothTransformation)
        label.setPixmap(pixmap)
        label.setFixedSize(new_w, new_h)

    # ---------------------------------------------
    #        POMOCNICZE
    # ---------------------------------------------
    def hideAllSliders(self):
        self.brightness_slider.setVisible(False)
        self.contrast_slider.setVisible(False)

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = ImageProcessor()
    window.show()
    sys.exit(app.exec_())
