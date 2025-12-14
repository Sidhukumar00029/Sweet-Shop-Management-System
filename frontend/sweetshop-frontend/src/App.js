import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import ManageSweets from "./pages/ManageSweets";
import AddSweet from "./pages/AddSweet";
import UpdateSweet from "./pages/UpdateSweet";
import PurchaseSweet from "./pages/PurchaseSweet";
import RestockSweet from "./pages/RestockSweet";

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/manage" element={<ManageSweets />} />
        <Route path="/add" element={<AddSweet />} />
        <Route path="/update/:id" element={<UpdateSweet />} />
        <Route path="/purchase/:id" element={<PurchaseSweet />} />
        <Route path="/restock/:id" element={<RestockSweet />} />
      </Routes>
    </Router>
  );
}

export default App;
