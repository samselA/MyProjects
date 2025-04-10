#### 🗺️ Self-Organizing Map (SOM / Kohonen Network)
Implemented a Self-Organizing Map (Kohonen network) from scratch for unsupervised data projection and clustering, with support for configurable topologies and neighborhood functions. The project focused on interpretability, cluster quality evaluation, and extension to hexagonal grids.

---

**🧱 KOH1: Basic SOM on Rectangular Grid**
Developed a Kohonen network where neurons are placed on a configurable M×N rectangular grid. Implemented two neighborhood functions: the Gaussian and its negative second derivative, both with dynamic neighborhood width control. The model was tested on 2D and 3D synthetic datasets clustered at the vertices of a hexagon and cube, respectively. Evaluated the network's ability to recover class-structured clusters by analyzing neuron assignments and intra-neuron class purity.

**🔷 KOH2: SOM on Hexagonal Grid**
Extended the SOM implementation to support hexagonal neuron topology and applied both neighborhood functions to real-world datasets: MNIST and the Human Activity Recognition dataset (UCI). Though labels were hidden during training, the resulting cluster map was evaluated post hoc for alignment with known class distributions. Observations included inter-class separation, intra-neuron homogeneity, and topological consistency.
