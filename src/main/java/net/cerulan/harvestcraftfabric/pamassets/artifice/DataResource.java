package net.cerulan.harvestcraftfabric.pamassets.artifice;

import com.swordglowsblue.artifice.api.resource.ArtificeResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DataResource implements ArtificeResource<byte[]> {
    private byte[] data;

    public DataResource(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public String toOutputString() {
        return "<binary data>";
    }

    @Override
    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.data);
    }
}
