import React, { useEffect, useState } from "react";
import SweetService from "../services/SweetService";

export default function SweetList() {
  const [sweets, setSweets] = useState([]);

  useEffect(() => {
    loadSweets();
  }, []);

  const loadSweets = () => {
    SweetService.getAllSweets()
      .then(res => setSweets(res.data))
      .catch(err => console.error(err));
  };

  const handleDelete = (id) => {
    SweetService.deleteSweet(id).then(() => loadSweets());
  };

  return (
    <div>
      <h2>Available Sweets</h2>

      <table border="1" cellPadding="10" style={{ width: "100%", marginTop: 20 }}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {sweets.map(s => (
            <tr key={s.id}>
              <td>{s.name}</td>
              <td>{s.category}</td>
              <td>{s.price}</td>
              <td>{s.quantity}</td>
              <td>
                <button onClick={() => handleDelete(s.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
