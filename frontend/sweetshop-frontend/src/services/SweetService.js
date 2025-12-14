import axios from "axios";

const BASE_URL = "http://localhost:8080/api/sweets";

class SweetService {
  getAllSweets() {
    return axios.get(BASE_URL);
  }

  searchSweets(name, category, minPrice, maxPrice) {
    return axios.get(`${BASE_URL}/search`, {
      params: { name, category, minPrice, maxPrice }
    });
  }

  addSweet(sweet) {
    return axios.post(BASE_URL, sweet);
  }

  updateSweet(id, sweet) {
    return axios.put(`${BASE_URL}/${id}`, sweet);
  }

  deleteSweet(id) {
    return axios.delete(`${BASE_URL}/${id}`);
  }

  purchaseSweet(id, qty) {
    return axios.post(`${BASE_URL}/${id}/purchase`, { quantityToPurchase: qty });
  }

  restockSweet(id, qty) {
    return axios.post(`${BASE_URL}/${id}/restock`, { quantityToRestock: qty });
  }
}

export default new SweetService();
