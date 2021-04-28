import React, {useState, useEffect} from 'react'
import axios from 'axios';
import MyDropZone from './MyDropZone';

const UserProfiles = () => {

  const [userProfiles, setUserProfiles] = useState([]);

  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/api/v1/user-profile").then(res => {
      console.log(res);
      setUserProfiles(res.data);
    });
  }

  useEffect(() => {
    fetchUserProfiles();
  }, []);

  return userProfiles.map((userProfile, index) => {
    return (
      <div key ={index}>
        {userProfile.userProfileId ? (
        <img src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileId}/image/download`} 
        alt=""/> 
         ) : null}
        <br/><br/>
        <h1>{userProfile.userName}</h1>
        <p>{userProfile.userProfileId}</p>
        <MyDropZone {...userProfile}/>
        <br/>
      </div>
    )
  })
};
  

  export default UserProfiles;