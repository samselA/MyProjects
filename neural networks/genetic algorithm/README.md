#### 🧬 Genetic Algorithm Projects
Implemented a modular genetic algorithm framework for solving diverse optimization problems. Core components include customizable mutation (Gaussian), crossover (one-point), selection, and encoding strategies. This project demonstrates both classical applications and integration with neural models.

---

**🧪 AE1: Function Optimization in R³ and R⁵**
Implemented a basic genetic algorithm with Gaussian mutation and one-point crossover. Validated the algorithm on simple continuous functions, including a 3D quadratic objective (`x² + y² + 2z²`) and the 5D Rastrigin function, analyzing convergence and robustness under different parameterizations.

**📦 AE2: Cutting Stock Problem – Rectangle Packing in Circle**
Formulated and solved a real-world variant of the cutting stock problem using evolutionary optimization. Designed a genetic representation of rectangle placements and implemented constraints ensuring axis alignment and non-overlapping conditions. The goal was to pack rectangles inside a circular area to maximize total value. 
- Developed a custom fitness function incorporating geometric feasibility and total value.
- Applied and tuned the solution on benchmark instances (e.g., `r800`, `r1200`, etc.) with performance targets (e.g., achieving 30,000+ total value).
- The algorithm supports reuse of shapes and handles varying rectangle sets per instance.
  
**🧠 AE3: Evolving MLP Weights with a Genetic Algorithm**
Extended the GA framework to train a fixed-topology MLP by directly optimizing its weight vector. Treated the network's performance (training set error) as the fitness function. 
- Applied to multiple datasets including:  
  - [Iris dataset](https://archive.ics.uci.edu/ml/datasets/Iris) (classification),  
  - [Auto MPG](https://archive.ics.uci.edu/ml/datasets/Auto+MPG) (regression),  
  - `multimodal-large` (from prior MLP experiments).
- Compared the GA-based training with gradient descent to highlight interpretability vs. performance trade-offs.
