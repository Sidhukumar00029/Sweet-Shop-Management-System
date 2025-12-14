import React, { useState, useEffect } from 'react';
import { Table, Button, Container, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import axios from 'axios';

// ... (previous imports remain the same)

const ManageSweets = () => {
  const [sweets, setSweets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchSweets = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/sweets');
      setSweets(response.data);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching sweets:', err);
      setError('Failed to load sweets. Please try again later.');
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSweets();
  }, []);

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this sweet?')) {
      try {
        await axios.delete(`http://localhost:8080/api/sweets/${id}`);
        // Refresh the sweets list after deletion
        fetchSweets();
      } catch (err) {
        console.error('Error deleting sweet:', err);
        setError('Failed to delete sweet. Please try again.');
      }
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Manage Sweets</h2>
        <Button as={Link} to="/add" variant="primary">
          Add New Sweet
        </Button>
      </div>
      
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Name</th>
            <th>Category</th>
            <th>Price (₹)</th>
            <th>Quantity</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {sweets.map((sweet) => (
            <tr key={sweet.id}>
              <td>{sweet.name}</td>
              <td>{sweet.category}</td>
              <td>₹{sweet.price?.toFixed(2)}</td>
              <td>{sweet.quantity}</td>
              <td>
                <Button 
                  as={Link} 
                  to={`/update/${sweet.id}`} 
                  variant="info" 
                  size="sm" 
                  className="me-2"
                >
                  Edit
                </Button>
                <Button 
                  as={Link} 
                  to={`/purchase/${sweet.id}`} 
                  variant="success" 
                  size="sm" 
                  className="me-2"
                >
                  Purchase
                </Button>
                <Button 
                  as={Link} 
                  to={`/restock/${sweet.id}`} 
                  variant="warning" 
                  size="sm"
                  className="me-2"
                >
                  Restock
                </Button>
                <Button 
                  variant="danger" 
                  size="sm"
                  onClick={() => handleDelete(sweet.id)}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

export default ManageSweets;