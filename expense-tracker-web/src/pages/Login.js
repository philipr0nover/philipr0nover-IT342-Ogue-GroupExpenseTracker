import { useState } from "react";
import { loginUser } from "../services/authService";
import { Link } from "react-router-dom";

function Login() {

  const [formData,setFormData] = useState({
    email:"",
    password:""
  });

  const handleChange = (e)=>{
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e)=>{
    e.preventDefault();

    try{
      await loginUser(formData);
      localStorage.setItem("user",formData.email);
      window.location.href="/dashboard";
    }catch{
      alert("Invalid login");
    }
  };

  return(

    <div className="login-container">

      <div className="login-card">

        <div className="left-panel">
          <h2>Welcome Back</h2>
          <p>
            Manage your group expenses easily and track
            shared payments with your friends.
          </p>
        </div>

        <div className="right-panel">

          <form className="login-form" onSubmit={handleSubmit}>

            <h2>Sign In</h2>

            <input
              className="login-input"
              name="email"
              placeholder="Enter email"
              onChange={handleChange}
            />

            <input
              className="login-input"
              type="password"
              name="password"
              placeholder="Enter password"
              onChange={handleChange}
            />

            <button className="login-button">
              LOGIN
            </button>

            <div className="login-link">
              <Link to="/register">Create an account</Link>
            </div>

          </form>

        </div>

      </div>

    </div>

  );
}

export default Login;