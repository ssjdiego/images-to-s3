import {useDropzone} from 'react-dropzone'
import React, {useCallback} from 'react'
import axios from 'axios';

const MyDropzone = ({userProfileId}) => {
    const onDrop = useCallback(acceptedFiles => {
        const file = acceptedFiles[0];
        console.log(file);
        const formData = new FormData();
        formData.append("file", file);
        axios.post(
            `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`,
            formData,
            {
                headers: {
                    "Content-type": "multipart/form-data"
                }
            }
            ).then(() => {
                console.log("File was successfully posted");
            }).catch (err => {
                console.log(err);
            });
    }, [userProfileId])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    return (
        <div {...getRootProps()}>
        <input {...getInputProps()} />
        {
            isDragActive ?
            <p>Drop the image here ...</p> :
            <p>Drag 'n' drop some files here, or click to select files</p>
        }
        </div>
    )
}

export default MyDropzone;