# 💼 Project Portfolio – Aleksandra Samsel

Welcome to my personal project showcase. This repository includes a selection of data science, machine learning, and other projects that demonstrate my ability to solve real-world problems with Python, analytical thinking, and modern ML techniques.

Each folder represents an independent project. Inside you'll find source code, documentation, and in many cases notebooks to help you explore the work.

---

## 📂 Projects Overview

### 🌍 Socio-Economic Data Warehouse & BI Analytics

**Objective:** Build a unified analytics platform to explore relationships between socio-economic development, sustainability, and subjective well-being across countries and time.

* 🗂️ Integrated public datasets from UNDP (HDI), World Bank (WDI), and the World Happiness Report into a single **data warehouse** with a star-schema model at country–year granularity.
* 🔄 Implemented end-to-end **ETL pipelines** using Python and SSIS, including data validation, transformations, pivoting, and incremental SQL-based loading.
* 📈 Developed **interactive Power BI dashboards** with DAX measures to analyze correlations between economic indicators, inequality, CO₂ emissions, digitalization, and happiness.
* 🎯 Enabled exploratory and hypothesis-driven analysis for use cases such as ESG benchmarking, policy analysis, and cross-country comparisons.

---

### 🧠 Neural Networks Projects
**Objective:** Explore and implement foundational neural network techniques and biologically inspired optimization algorithms, with a focus on interpretability, hands-on implementation, and experimentation. Each project contains detailed report discussing implementation choices, hyperparameter sensitivity, and experimental findings

This section includes three independent projects:

#### 🔬 Multilayer Perceptron (MLP) – From Scratch
- 👷 Built a fully connected feedforward neural network from the ground up using only `NumPy`, without high-level ML libraries.
- 🧠 Focused on visualizing internal parameter changes and understanding the learning dynamics under different hyperparameter settings.
- 🔄 Conducted controlled experiments to observe how learning rate, weight initialization, and network depth affect training behavior and prediction accuracy.

➡️ [More in folder](./neural%20networks/multilayer%20perceptron)

#### 🗺️ Self-Organizing Map (SOM / Kohonen Network)
- 📉 Implemented a 2D self-organizing map for unsupervised learning and data projection.
- 🧪 Applied to image datasets like MNIST and notMNIST to explore topological preservation and clustering behavior without label supervision.
- 🧭 Analyzed the ability of the network to distinguish and map high-dimensional input vectors onto a low-dimensional grid structure.

➡️ [More in folder](./neural%20networks/self-organizing%20map)

#### 🧬 Genetic Algorithm for Cutting Stock Problem
- 🔁 Implemented a genetic algorithm to solve multi-variable optimization problems, focusing on the **cutting stock problem** — maximizing material usage by fitting predefined shapes into containers (e.g., rectangles into a circle).
- 🧪 Designed a custom fitness function, crossover logic, and mutation scheme tailored to spatial packing efficiency.
- 📦 Compared algorithm behavior on different data sets, with final tests integrating crossover strategies informed by MLP output.

➡️ [More in folder](./neural%20networks/genetic%20algorithm)

---

### 🐟 Fish and Ship Tracking Application
**Objective:** Developed an application for real-time tracking of fish and ships globally, leveraging a user-friendly JavaFX interface.

- 🌍 Integrated with the [Global Fishing Watch API](https://globalfishingwatch.org/our-apis/) to retrieve and display real-time locations and movement paths of registered vessels worldwide.
- 🧭 Offered users an interactive and dynamic visualization of ship and fish tracking data.
- ⚙️ Focused on seamless API integration and responsive UI using JavaFX to ensure smooth user experience.

➡️ [More in folder](./fish%20tracking%20app)

---

### 🏦 Bankruptcy Prediction
**Objective:** Predict company bankruptcy using financial indicators from a real-world Kaggle dataset.  
[Dataset Link](https://www.kaggle.com/datasets/fedesoriano/company-bankruptcy-prediction)

- 🔍 **Data Exploration:** Identified key financial metrics influencing bankruptcy risk, such as profitability ratios, leverage, and liquidity indicators.
- 🧱 **Feature Engineering:** Prepared data for modeling by handling missing values, normalizing input features, and generating derived indicators when necessary.
- 🤖 **Modeling and Validation:** Applied classification algorithms (e.g., logistic regression, decision trees, ensemble models) to predict bankruptcy and evaluated their performance using metrics like AUC-ROC and precision-recall.

➡️ [More in folder](./bankruptcy%20prediction)

---

### 📰 News / Text Classification
**Objective:** Perform document classification using natural language processing techniques to analyze, extract, and model textual content.

- 🔗 **Dataset:** [Text Classification Documentation – Kaggle](https://www.kaggle.com/datasets/ramjaslangdi/text-classification-documentation)
- 🔍 **Exploratory Analysis:** Analyzed the content of documents with a focus on word distributions and thematic structures.
- 🧱 **Feature Engineering:** Applied standard NLP techniques including TF-IDF vectorization and sentiment analysis to extract meaningful features from raw text.
- 🧠 **Modeling and Validation:** Used classification and clustering algorithms to identify dominant topics or assign documents to predefined categories based on semantic similarity.

➡️ [More in folder](./news%20classification)

---

### 🍽️ Food Data Visualization
**Objective:** Visualize and analyze the environmental impact of global food production using interactive and insightful graphics.

- 🌱 Based on the [Environmental Impact of Food Production](https://www.kaggle.com/datasets/selfvivek/environment-impact-of-food-production/) dataset from Kaggle, covering metrics such as land use, greenhouse gas emissions, water usage, and eutrophication.
- 📊 Built clear and engaging visualizations using Python libraries like `Matplotlib`, `Seaborn`, and `Plotly` to explore how different food products compare across sustainability dimensions.
- 🎯 Highlighted key insights into the trade-offs between food categories (e.g., meat vs. plant-based) and their ecological footprints to support better awareness and decision-making.

➡️ [More in folder](./food%20data%20visualization)

---

### 🖼️ Image Processing Application
**Objective:** Implement and demonstrate fundamental image processing techniques through an interactive desktop application.

- ⚙️ Built using Python with `NumPy`, `OpenCV`, `Matplotlib`, and `PyQt5` to provide matrix-based image operations, filter applications, and histogram analysis via a graphical interface.
- 🖌️ Supported pixel-level operations such as brightness and contrast adjustment, grayscale conversion, and negative image generation.
- 🧠 Implemented convolution-based filters including blurring, sharpening, and edge detection.
- 📊 Visualized histograms and image projections; included modules for geometric transformations such as rotation and mirror flipping.
- 🖼️ GUI includes functional tabs for image selection, transformation, pixel operations, filters, and analytical visualizations.

---

