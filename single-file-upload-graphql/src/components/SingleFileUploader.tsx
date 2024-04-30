import React, { useState } from "react";
import { useMutation, gql } from '@apollo/client';

const UPLOAD_FILE = gql`
  mutation UploadFileGrpc($uploadPdfRequest: UploadPdfRequestInput!) {
    uploadPdfGrpc(uploadPdfRequest: $uploadPdfRequest) {
      metaData{
        title
        type
      }
      msg
    }
  }
`;

const toBase64 = (file: File): Promise<string> => {
  return new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (typeof reader.result === 'string') {
        resolve(reader.result);
      } else {
        reject(new Error('Failed to convert file to base64'));
      }
    };
    reader.onerror = (error) => {
      reject(error);
    };
  });
};

interface SingleFileUploaderProps {
    onUploadFinish: () => void;
}

const SingleFileUploader:React.FC<SingleFileUploaderProps> = (props) => {
  const [uploadFile, setFile] = useState<File | null>(null);
  const [uploadPdf] = useMutation(UPLOAD_FILE);

  const [status, setStatus] = useState<
    "initial" | "uploading" | "success" | "fail"
  >("initial");

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setStatus("initial");
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (uploadFile) {
      const base64String = await toBase64(uploadFile)
      console.log(base64String)
      const formattedString = base64String.substring(base64String.indexOf(",")+1)
      setStatus("uploading")
      try {
       const response = await uploadPdf({
          variables: {
            uploadPdfRequest: {
              file: formattedString,
              contentType: uploadFile.type,
              name: uploadFile.name
            }
          }
        });

        props.onUploadFinish();
        setStatus("success");
      } catch (error) {
        console.error(error);
        setStatus("fail");
      }
    }
  };

  return (
    <>
      <div className="input-group">
        <h3>Upload a File</h3>
        <label htmlFor="file" className="sr-only">
          Choose a file
        </label>
        <input id="file" type="file" onChange={handleFileChange} />
      </div>
      {uploadFile && (
        <section>
          File details:
          <ul>
            <li>Name: {uploadFile.name}</li>
            <li>Type: {uploadFile.type}</li>
            <li>Size: {uploadFile.size} bytes</li>
          </ul>
        </section>
      )}

      {uploadFile && (
        <button onClick={handleUpload} className="submit">
          Upload a file
        </button>
      )}

      <Result status={status} />
    </>
  );
};

const Result = ({ status }: { status: string }) => {
  if (status === "success") {
    return <p>✅ File uploaded successfully!</p>;
  } else if (status === "fail") {
    return <p>❌ File upload failed!</p>;
  } else if (status === "uploading") {
    return <p>⏳ Uploading selected file...</p>;
  } else {
    return null;
  }
};

export default SingleFileUploader;
