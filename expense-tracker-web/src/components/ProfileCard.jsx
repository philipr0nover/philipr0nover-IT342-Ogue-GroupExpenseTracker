function ProfileCard(){

  const user = localStorage.getItem("user");

  return(

    <div className="profile-card">

      <div className="profile-avatar">
        {user ? user.charAt(0).toUpperCase() : "U"}
      </div>

      <h2>{user}</h2>

      <p>Member</p>

    </div>

  );
}

export default ProfileCard;