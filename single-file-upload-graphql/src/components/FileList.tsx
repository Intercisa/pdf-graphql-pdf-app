import React, { useState, useEffect } from 'react';
import FileType from '../models/FileType';
import ListItem from '../models/ListItem'
import DownloadableItem from './DownloadableItem'
import { useQuery, gql } from '@apollo/client';

interface FileListProps {
  fileType: FileType,
  refresh: boolean
}

interface ListFileResponse{
  listFilesGrpc: ListItem[]
}

const GET_FILES = gql`
query ListFilesGrpc($bucketName: String!) {
  listFilesGrpc(bucketName: $bucketName) {
      id
      name
      size
      url
  }
}
`;
function setListTitle(fileType: FileType) {
    if (fileType === FileType.PDF) {
      return "PDF List"
    } 
    return "Picture List"
}

const FileList: React.FC<FileListProps> = (props) => {
  const { loading, error, data, refetch } = useQuery<ListFileResponse>(GET_FILES, {
    variables: { bucketName: props.fileType === FileType.PDF ? "pdf-bucket" : "pdf-image-bucket" }
  });
  const title = setListTitle(props.fileType)

  useEffect(() => {
    if (props.refresh) {
      refetch();
    }
  }, [props.refresh, refetch]);


  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;
  const files = data?.listFilesGrpc || [];

  return (
    <div>
      <h2>
        {title} 
      </h2>
      <ul>
        {files.map((item) => (
             <li key={item.id}>
             <DownloadableItem id={item.id} name={item.name} size={item.size} url={item.url} />
           </li>
        ))}
      </ul>
    </div>
  );
};

export default FileList;
