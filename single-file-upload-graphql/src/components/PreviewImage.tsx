import React from 'react';

interface PreviewProps {
    imageUrl: string
}

const PreviewImage:React.FC<PreviewProps> = (props) => {
  return (
    <div>
      <h2>Preview Image</h2>
      <img src={props.imageUrl} alt="Preview" />
    </div>
  );
};

export default PreviewImage;