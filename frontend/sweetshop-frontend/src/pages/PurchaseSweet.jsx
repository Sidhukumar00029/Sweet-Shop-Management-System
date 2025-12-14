import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function PurchaseSweet() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [sweet, setSweet] = useState(null);
  const [qty, setQty] = useState("");

  useEffect(() => {
    fetch(`http://localhost:8080/api/sweets/${id}`)
      .then(res => res.json())
      .then(data => setSweet(data));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    await fetch(`http://localhost:8080/api/sweets/${id}/purchase`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ quantityToPurchase: parseInt(qty) })
    });

    alert("Purchased!");
    navigate("/manage");
  };

  if (!sweet) return <h3>Loading...</h3>;

  return (
    <div style={{ padding: "20px" }}>
      <h2>Purchase Sweet</h2>

      <p>Name: {sweet.name}</p>
      <p>Available Quantity: {sweet.quantity}</p>

      <form onSubmit={handleSubmit}>
        <label>Purchase Quantity:</label>
        <input type="number" onChange={(e) => setQty(e.target.value)} />

        <button type="submit">Purchase</button>
      </form>
    </div>
  );
}

export default PurchaseSweet;
