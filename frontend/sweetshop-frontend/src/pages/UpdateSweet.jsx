import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function UpdateSweet() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "", category: "", price: "", quantity: ""
  });

  useEffect(() => {
    fetch(`http://localhost:8080/api/sweets/${id}`)
      .then(res => res.json())
      .then(data => setFormData(data));
  }, []);

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();

    await fetch(`http://localhost:8080/api/sweets/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData)
    });

    alert("Updated!");
    navigate("/manage");
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Update Sweet</h2>

      <form style={{ width: "300px" }} onSubmit={handleSubmit}>
        <label>Name:</label>
        <input name="name" value={formData.name} onChange={handleChange} />

        <label>Category:</label>
        <input name="category" value={formData.category} onChange={handleChange} />

        <label>Price:</label>
        <input name="price" value={formData.price} onChange={handleChange} />

        <label>Quantity:</label>
        <input name="quantity" value={formData.quantity} onChange={handleChange} />

        <br /><br />
        <button type="submit">Update</button>
      </form>
    </div>
  );
}

export default UpdateSweet;
