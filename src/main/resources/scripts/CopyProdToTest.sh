cd /home/batch/theses-misc

/usr/java/jdk-11.0.2/bin/java -Djava.security.egd=file:///dev/urandom -jar theses-misc.jar --spring.batch.job.names=copyProdToTest >> logBatch.txt > logCopyProdToTest.txt &