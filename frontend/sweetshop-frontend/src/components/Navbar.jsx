import { Link, useLocation } from "react-router-dom";

function Navbar() {
  const location = useLocation();

  const linkStyle = (path) => ({
    color: location.pathname === path ? "#0d6efd" : "#333",
    textDecoration: "none",
    fontWeight: "500",
    fontSize: "22px",
    padding: "6px 12px",
    borderRadius: "6px",
    backgroundColor: location.pathname === path ? "#e3f2fd" : "transparent",
    transition: "all 0.2s ease"
  });

  return (
    <nav
      style={{
        height: "60px",
        background: "aqua",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        borderBottom: "1px solid #ddd"
      }}
    >
      <div
        style={{
          display: "flex",
          gap: "20px"
        }}
      >
        <Link to="/" style={linkStyle("/")}>
          Home
        </Link>

        <Link to="/manage" style={linkStyle("/manage")}>
          Manage
        </Link>

        <Link to="/add" style={linkStyle("/add")}>
          Add Sweet
        </Link>
      </div>
    </nav>
  );
}

export default Navbar;
