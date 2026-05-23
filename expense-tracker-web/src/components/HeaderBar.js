import React from "react";

function HeaderBar({ title }) {

  const userData = localStorage.getItem("user");
  const user = userData ? JSON.parse(userData) : null;

  return (

    <div className="header-bar">

      <h1>{title}</h1>

      <div className="header-user">

        <div className="user-avatar">
          {user?.firstname?.charAt(0).toUpperCase() || "U"}
        </div>

        <span>
          {user?.email || "Guest"}
        </span>

      </div>

    </div>

  );

}

export default HeaderBar;