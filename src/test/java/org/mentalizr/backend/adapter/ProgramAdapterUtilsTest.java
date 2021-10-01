package org.mentalizr.backend.adapter;

import org.junit.jupiter.api.Test;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.mentalizr.serviceObjects.frontend.program.ProgramSOX;
import org.mentalizr.serviceObjects.frontend.program.StepSO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProgramAdapterUtilsTest {

    @Test
    public void buildStepSOList() {

        // json from m7r-backend-test/T01_ProgramTest
        String programJson = "{\n" +
                "    \"blocking\": true,\n" +
                "    \"id\": \"test\",\n" +
                "    \"infotexts\": [\n" +
                "        {\n" +
                "            \"id\": \"test__info_1\",\n" +
                "            \"name\": \"Dummyinfotext1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"test__info_10\",\n" +
                "            \"name\": \"Infoseite Test1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"test__info_11\",\n" +
                "            \"name\": \"Zweite Infoseite-Test\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"test__info_2\",\n" +
                "            \"name\": \"Dummyinfotext2\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"modules\": [\n" +
                "        {\n" +
                "            \"id\": \"test_m1\",\n" +
                "            \"name\": \"Eingabefelder\",\n" +
                "            \"submodules\": [\n" +
                "                {\n" +
                "                    \"id\": \"test_m1_sm1\",\n" +
                "                    \"name\": \"page scope\",\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm1_s1\",\n" +
                "                            \"name\": \"Schritt 1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm1_s2\",\n" +
                "                            \"name\": \"Schritt 2\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"test_m1_sm2\",\n" +
                "                    \"name\": \"generic program scope\",\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm2_s1\",\n" +
                "                            \"name\": \"Schritt 1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm2_s2\",\n" +
                "                            \"name\": \"Schritt 2\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm2_s3\",\n" +
                "                            \"name\": \"Schritt 3\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"test_m1_sm3\",\n" +
                "                    \"name\": \"program scope\",\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm3_s1\",\n" +
                "                            \"name\": \"Schritt 1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm3_s2\",\n" +
                "                            \"name\": \"Schritt 2\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm3_s3\",\n" +
                "                            \"name\": \"Schritt 3\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"test_m1_sm4\",\n" +
                "                    \"name\": \"program scope inc\",\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm4_s1\",\n" +
                "                            \"name\": \"Schritt 1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm4_s2\",\n" +
                "                            \"name\": \"Schritt 2\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m1_sm4_s3\",\n" +
                "                            \"name\": \"Schritt 3\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"test_m2\",\n" +
                "            \"name\": \"Dummy\",\n" +
                "            \"submodules\": [\n" +
                "                {\n" +
                "                    \"id\": \"test_m2_sm1\",\n" +
                "                    \"name\": \"Erstes Submodul\",\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m2_sm1_s1\",\n" +
                "                            \"name\": \"Schritt 1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"feedback\": true,\n" +
                "                            \"id\": \"test_m2_sm1_s2\",\n" +
                "                            \"name\": \"Schritt 2\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"test_m2_sm2\",\n" +
                "                    \"name\": \"Zweites Submodul\",\n" +
                "                    \"steps\": [\n" +
                "                        {\n" +
                "                            \"feedback\": false,\n" +
                "                            \"id\": \"test_m2_sm2_s1\",\n" +
                "                            \"name\": \"Schritt 1\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"name\": \"Test\"\n" +
                "}";

        ProgramSO programSO = ProgramSOX.fromJson(programJson);

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertEquals(14, stepSOList.size());


        System.out.println("size: " + stepSOList.size());
        for (StepSO stepSO : stepSOList) {
            System.out.println(stepSO.getId());
        }


    }

}