package org.drools.example;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@Disabled
@NativeImageTest
public class NativeHL7v2ResourceIT extends HL7v2ResourceTest {

    // Execute the same tests but in native mode.
}