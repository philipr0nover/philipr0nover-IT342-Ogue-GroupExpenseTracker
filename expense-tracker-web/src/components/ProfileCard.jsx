function ProfileCard(){

  const user = localStorage.getItem("user");

  return(

    <div className="profile-card">

      <div className="profile-avatar">
        {user ? user.charAt(0).toUpperCase() : "U"}
      </div>

      <h2>{user}</h2>

      <p>Expense Tracker Member</p>

      <button className="profile-edit-btn">
        Edit Profile
      </button>

    </div>

  );

}

export default ProfileCard;