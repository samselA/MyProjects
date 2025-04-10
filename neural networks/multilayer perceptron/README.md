#### 🔬 Multilayer Perceptron (MLP) – From Scratch
This project consists of a series of progressively advanced implementations of a feedforward neural network, developed entirely from scratch using only `NumPy`. The work focused on flexibility, experimental depth, and step-by-step enhancement of training capabilities.

---

**🧱 NN1: Baseline Implementation**
Implemented a flexible MLP architecture supporting configurable numbers of layers, neurons, and activation functions. The model was applied to simple regression tasks with manual tuning of weights and architecture. Network performance was evaluated on datasets like `square-simple` and `steps-large`.

**🧠 NN2: Backpropagation**
Developed backpropagation from scratch with support for both batch and full-batch learning. Included weight visualization to aid in debugging and understanding training dynamics. Models were tested on increasingly complex datasets, such as `multimodal-large`, to ensure convergence and correctness.

**⚙️ NN3: Momentum and Gradient Normalization**
Extended the gradient descent algorithm with momentum and RMSProp optimizers. Compared convergence speed and performance across datasets using MSE metrics. Results demonstrated improvements in training stability and effectiveness on complex regression problems.

**📊 NN4: Classification with Softmax**
Implemented softmax activation and adjusted the learning algorithm for multi-class classification. Conducted comparative experiments with and without softmax on datasets like `rings3-regular`, evaluating models using the F-measure to assess classification performance.

**🔁 NN5: Activation Function Comparison**
Integrated multiple activation functions (sigmoid, linear, tanh, ReLU) and tested their effect on network performance. Compared training speed and model quality across different network depths. Results highlighted the importance of function choice in deeper architectures.

**📉 NN6: Overfitting & Regularization**
Introduced weight regularization and early stopping based on validation loss to mitigate overfitting. Assessed generalization performance on sparse and balanced datasets. Observed how different regularization strategies affect learning in low-data or noisy scenarios.
