package au.org.emii.netcdfsubset

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class SubsetService {

    def subset(typeName, cqlFilter, response) {
        //TODO: replace with Julian's code
        ZipOutputStream zos = new ZipOutputStream(response)
        ZipEntry entry = new ZipEntry("TestFile.nc")
        String content = "Some test content\n"
        entry.setSize(content.length())
        zos.putNextEntry(entry)
        zos.write(content.getBytes());
        zos.closeEntry();
        zos.close();
    }
}
