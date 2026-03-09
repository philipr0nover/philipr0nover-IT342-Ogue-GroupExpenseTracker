function HeaderBar({ title }){

  const user = localStorage.getItem("user");

  return(

    <div className="header-bar">

      <h1>{title}</h1>

      <div className="header-user">
        <div className="user-avatar">
          {user ? user.charAt(0).toUpperCase() : "U"}
        </div>

        <span>{user}</span>
      </div>

    </div>

  );

}

export default HeaderBar;