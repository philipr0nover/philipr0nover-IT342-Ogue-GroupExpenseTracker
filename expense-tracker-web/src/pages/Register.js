import { useState } from "react";
import { registerUser } from "../services/authService";
import { Link } from "react-router-dom";

function Register(){

  const [formData,setFormData] = useState({
    firstname:"",
    lastname:"",
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
      await registerUser(formData);
      alert("Registration successful");
      window.location.href="/";
    }catch{
      alert("Registration failed");
    }
  };

  return(

    <div className="register-container">

      <div className="register-card">

        {/* LEFT SIDE FORM */}
        <div className="register-left">

          <form className="register-form" onSubmit={handleSubmit}>

            <h2>Create Account</h2>

            <input
              className="register-input"
              name="firstname"
              placeholder="First Name"
              onChange={handleChange}
            />

            <input
              className="register-input"
              name="lastname"
              placeholder="Last Name"
              onChange={handleChange}
            />

            <input
              className="register-input"
              name="email"
              placeholder="Email"
              onChange={handleChange}
            />

            <input
              className="register-input"
              type="password"
              name="password"
              placeholder="Password"
              onChange={handleChange}
            />

            <button className="register-button">
              REGISTER
            </button>

            <div className="register-link">
              <Link to="/">Already have an account? Login</Link>
            </div>

          </form>

        </div>

        {/* RIGHT SIDE PANEL */}
        <div className="register-right">

          <h2>Join the Tracker</h2>

          <p>
            Create an account and start managing shared expenses
            with your friends. Track payments and split costs easily.
          </p>

        </div>

      </div>

    </div>
  );
}

export default Register;
