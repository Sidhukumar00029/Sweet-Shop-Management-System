import { Container, Card } from "react-bootstrap";

function Home() {
  return (
    <div
      style={{
        minHeight: "calc(100vh - 60px)",
        background: "linear-gradient(135deg, #f8f9fa, #e3f2fd)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center"
      }}
    >
      <Container className="d-flex justify-content-center">
        <Card
          className="p-4 text-center shadow-lg"
          style={{
            maxWidth: "500px",
            borderRadius: "12px",
            border: "none"
          }}
        >
          <h2 style={{ color: "#0d6efd", fontWeight: "700", fontSize: "30px" }}>
            Sweet Shop Management System
          </h2>
          <hr />
          

          <p style={{ color: "#555", fontSize: "18px"  }}>
            Easily manage sweets, inventory, purchases, and restocking
            with a clean and simple dashboard.
          </p>
        </Card>
      </Container>
    </div>
  );
}

export default Home;
