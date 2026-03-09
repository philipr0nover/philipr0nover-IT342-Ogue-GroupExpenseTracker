import { Link } from "react-router-dom";

function Navbar() {

  const logout = () => {
    localStorage.removeItem("user");
    window.location.href = "/";
  };

  return (
    <nav style={{background:"#2ecc71", padding:"10px"}}>

      <h2 style={{display:"inline", marginRight:"20px"}}>
        Expense Tracker
      </h2>

      <Link to="/dashboard" style={{marginRight:"15px"}}>
        Dashboard
      </Link>

      <button onClick={logout}>
        Logout
      </button>

    </nav>
  );
}

export default Navbar;