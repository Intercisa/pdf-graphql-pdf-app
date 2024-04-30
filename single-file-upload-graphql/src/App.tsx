import SingleFileUploader from './components/SingleFileUploader'
import FileList from './components/FileList'
import React, { useState } from 'react';
import FileType from './models/FileType';
import './App.css';

function App() {

  const [refresh, setRefresh] = useState<boolean>(false);

  const handleRefreshLists = () => {
    setRefresh(true);
  };

  {console.log(refresh)}

  return (
    <>
    <h1>PDF to Image</h1>
      <div className="container">
      <div className="column">
          <SingleFileUploader onUploadFinish={handleRefreshLists} />
        </div>
        <div className="column">
          <FileList fileType={FileType.PDF} refresh={refresh}/>
          <FileList fileType={FileType.PICTURE} refresh={refresh}/>
        </div>
    </div>
    </>
  )
}

export default App;
