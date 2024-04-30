- Minio 
   - ``` Docker for raspberry Pi : docker run -p 9000:9000 -p 9001:9001 --user $(id -u):$(id -g) --name minio1 -e "MINIO_ACCESS_KEY=MINIO" -e "MINIO_SECRET_KEY=SECRET_SECRET" -v ${HOME}/minio/data:/data minio/minio:RELEASE.2020-11-25T22-36-25Z-arm server /data ```

- Redis
   - ``` Docker : docker run -d -p 6379:6379 --name redis redis ```
