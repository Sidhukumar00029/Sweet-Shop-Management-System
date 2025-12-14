import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import axios from 'axios';

const AddSweet = () => {
  const [formData, setFormData] = useState({
    name: '',
    category: '',
    price: '',
    quantity: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  const { name, category, price, quantity } = formData;

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const config = {
        headers: {
          'Content-Type': 'application/json'
        }
      };

      const response = await axios.post('http://localhost:8080/api/sweets', formData, config);
      
      setSuccess('Sweet added successfully!');
      setError('');
      
      // Clear form
      setFormData({
        name: '',
        category: '',
        price: '',
        quantity: ''
      });
      
      // Redirect to manage page after 2 seconds
      setTimeout(() => {
        navigate('/manage');
      }, 2000);
      
    } catch (err) {
      setError(err.response?.data?.message || 'Error adding sweet');
      setSuccess('');
    }
  };

  return (
    <Container className="mt-4">
      <h2>Add New Sweet</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}
      
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Sweet Name</Form.Label>
          <Form.Control
            type="text"
            name="name"
            value={name}
            onChange={handleChange}
            placeholder="Enter sweet name"
            required
          />
        </Form.Group>
        
        <Form.Group className="mb-3">
          <Form.Label>Category</Form.Label>
          <Form.Select
            name="category"
            value={category}
            onChange={handleChange}
            required
          >
            <option value="">Select a category</option>
            <option value="Chocolate">Chocolate</option>
            <option value="Candy">Candy</option>
            <option value="Laddu">Laddu</option>
            <option value="Barfi">Barfi</option>
            <option value="Jalebi">Jalebi</option>
            <option value="Other">Other</option>
          </Form.Select>
        </Form.Group>
        
        <Form.Group className="mb-3">
          <Form.Label>Price (₹)</Form.Label>
          <Form.Control
            type="number"
            name="price"
            value={price}
            onChange={handleChange}
            placeholder="Enter price"
            min="0"
            step="0.01"
            required
          />
        </Form.Group>
        
        <Form.Group className="mb-3">
          <Form.Label>Quantity</Form.Label>
          <Form.Control
            type="number"
            name="quantity"
            value={quantity}
            onChange={handleChange}
            placeholder="Enter quantity"
            min="0"
            required
          />
        </Form.Group>
        
        <Button variant="primary" type="submit">
          Add Sweet
        </Button>
      </Form>
    </Container>
  );
};

export default AddSweet;
