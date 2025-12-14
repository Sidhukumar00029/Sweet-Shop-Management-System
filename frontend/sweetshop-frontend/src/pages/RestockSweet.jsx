import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Container, Card, Form, Button } from "react-bootstrap";


function RestockSweet() {
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

    await fetch(`http://localhost:8080/api/sweets/${id}/restock`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ quantityToAdd: parseInt(qty) })
    });

    alert("Restocked!");
    navigate("/manage");
  };

  if (!sweet) return <h3>Loading...</h3>;

  return (
  <Container className="d-flex justify-content-center mt-5">
    <Card className="p-4 shadow" style={{ width: "400px" }}>
      <h4 className="mb-3">Restock Sweet</h4>

      <p><strong>Name:</strong> {sweet.name}</p>
      <p><strong>Current Quantity:</strong> {sweet.quantity}</p>

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Quantity to Add</Form.Label>
          <Form.Control
            type="number"
            required
            onChange={(e) => setQty(e.target.value)}
          />
        </Form.Group>

        <Button type="submit" variant="warning" className="w-100">
          Restock
        </Button>
      </Form>
    </Card>
  </Container>
);

}

export default RestockSweet;
