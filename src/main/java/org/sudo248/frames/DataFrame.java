package org.sudo248.frames;

import org.sudo248.common.Opcode;
import org.sudo248.exceptions.InvalidDataException;

public abstract class DataFrame extends AbstractFrameDataImpl {
    public DataFrame(Opcode opcode) {
        super(opcode);
    }

    @Override
    public void isValid() throws InvalidDataException {

    }
}
