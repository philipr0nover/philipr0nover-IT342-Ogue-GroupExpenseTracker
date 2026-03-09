import { Link } from "react-router-dom";

function Sidebar(){

  const logout = ()=>{
    localStorage.removeItem("user");
    window.location.href="/";
  };

  return(

    <div className="sidebar">

      <h2>ExpenseTracker</h2>

      <Link to="/dashboard">Dashboard</Link>
      <Link to="/profile">Profile</Link>
      <Link to="#">Groups</Link>
      <Link to="#">Expenses</Link>

      <br/>

      <button onClick={logout}>
        Logout
      </button>

    </div>

  );
}

export default Sidebar;