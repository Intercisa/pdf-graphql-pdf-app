import React from 'react';
import ListItem from '../models/ListItem'
import './DownloadableItem.css';

const DownloadableItem: React.FC<ListItem> = (props) => {
  const handleDownload = async () => {
    try {
      // Fetch the file content from the URL
      const response = await fetch(props.url);
      if (!response.ok) {
        throw new Error('Failed to fetch file');
      }

      // Get the blob from the response
      const blob = await response.blob();

      // Create a URL for the Blob object
      const url = window.URL.createObjectURL(blob);

      // Create a temporary anchor element
      const a = document.createElement('a');
      a.href = url;
      a.download = props.name; // Set the file name

      // Append the anchor element to the document body
      document.body.appendChild(a);

      // Trigger a click event on the anchor element
      a.click();

      // Remove the anchor element from the document body
      document.body.removeChild(a);

      // Revoke the URL to release the memory
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  };

  return (
    <div className="downloadable-item">
      <a className="item-name" href={props.url} target="_blank">{props.name}</a>
      <p className="item-size">Size: {props.size}</p>
      <button className="download-button" onClick={handleDownload}>
        Download
      </button>
    </div>
  );
};

export default DownloadableItem;