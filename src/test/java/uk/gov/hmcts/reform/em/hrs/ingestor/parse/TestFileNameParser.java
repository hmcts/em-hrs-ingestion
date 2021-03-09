package uk.gov.hmcts.reform.em.hrs.ingestor.parse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

@Slf4j
public class TestFileNameParser {

    @ParameterizedTest(name = "Invalid parameter test : {0} --> {1}")
    @CsvSource(value = {"Empty Value,''", "Spaced Value,' '", "Value Value,NIL"}, nullValues = "NIL")
    public void test_negative_invalid_file_name_input(final String inputKey, final String inputValue) {
        try {
            FileNameParser.parseFileName(inputValue);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals("The argument passed is not valid",
                    illegalArgumentException.getLocalizedMessage());
        }

    }

    @ParameterizedTest(name = "Wrong Format File Names : {0} --> {1}")
    @CsvFileSource(resources = "/uk/gov/hmcts/reform/em/hrs/hrs/ingestor/data/invalid-file-format-values.csv", numLinesToSkip = 1)
    public void test_negative_wrong_format_file_name_input(final String inputKey, final String inputValue) {
        Map<String, Object> responseMap = FileNameParser.parseFileName(inputValue);
        assertEquals(1, responseMap.size());
        assertEquals(Arrays.stream(inputValue.split("_")).findFirst().get(),
                responseMap.get("CaseID").toString().trim());
    }

    @ParameterizedTest(name = "Positive tests for Civil and Family : {0} --> {1}")
    @CsvSource(value = {
            "Valid File Name All Capital Case,CV-0150-GN00NT095_2020-07-29-09.03.40.236-UTC_0",
            "Valid File Case Reference Small Case,CV-0291-g20yj687_2020-09-01-09.14.47.314-UTC_0",
            "Valid File Case Reference Jurisdiction Lower Case,cv-0144-CD0XT621_2020-10-20-14.05.10.150-UTC_0",
            "Valid File Case Reference Case Reference Mixed Case,CV-0150-GH0YN819Part1_2020-07-29-09.03.40.236-UTC_0",
            "Valid File Case Hyphenated 1,FM-0291-OX20P00022-Keene-v-England_2020-06-30-10.30.18.388-UTC_0",
            "Valid File Case Hyphenated 2,CV-0291-IP-2019-000175-DPA-v-Dagyanno_2020-07-02-09.23.23.994-UTC_0",
            "Valid File Location Code 3 Digits 1,FM-211-GU20C0090_2020-10-01-08.12.06.568-UTC_1",
            "Valid File Location Code 3 Digits 2,CP-005-13605371-AB_2020-09-10-13.18.39.768-UTC_0"
            },
            nullValues = "NIL")
    public void test_positive_location_code_based_input_for_civil_and_family(final String inputKey, final String inputValue) {

        Map<String, Object> responseMap = FileNameParser.parseFileName(inputValue);
        switch (inputKey) {
            case "Valid File Name All Capital Case":
                verifyValuesOfMappedResponse(responseMap, "CV", "150",
                        "GN00NT095", "0", 2020, 7, 29,
                        9, 3, 40, 236000000);
                break;
            case "Valid File Case Reference Small Case":
                verifyValuesOfMappedResponse(responseMap, "CV", "291",
                        "g20yj687", "0", 2020, 9, 1,
                        9, 14, 47, 314000000);
                break;
            case "Valid File Case Reference Jurisdiction Lower Case":
                verifyValuesOfMappedResponse(responseMap, "cv", "144",
                        "CD0XT621", "0", 2020, 10, 20,
                        14, 5, 10, 150000000);
                break;
            case "Valid File Case Reference Case Reference Mixed Case":
                verifyValuesOfMappedResponse(responseMap, "CV", "150",
                        "GH0YN819Part1", "0", 2020, 7, 29,
                        9, 3, 40, 236000000);
                break;
            case "Valid File Case Hyphenated 1":
                verifyValuesOfMappedResponse(responseMap, "FM", "291",
                        "OX20P00022-Keene-v-England", "0", 2020, 6, 30,
                        10, 30, 18, 388000000);
                break;
            case "Valid File Case Hyphenated 2":
                verifyValuesOfMappedResponse(responseMap, "CV", "291",
                        "IP-2019-000175-DPA-v-Dagyanno", "0", 2020, 7, 2,
                        9, 23, 23, 994000000);
                break;
            case "Valid File Location Code 3 Digits 1":
                verifyValuesOfMappedResponse(responseMap, "FM", "211",
                        "GU20C0090", "1", 2020, 10, 1,
                        8, 12, 6, 568000000);
                break;
            case "Valid File Location Code 3 Digits 2":
                verifyValuesOfMappedResponse(responseMap, "CP", "005",
                        "13605371-AB", "0", 2020, 9, 10,
                        13, 18, 39, 768000000);
                break;
            default:
                throw new IllegalArgumentException("Invalid key request for the Tests ");
        }
    }


    @ParameterizedTest(name = "Positive tests for Tribunals : {0} --> {1}")
    @CsvSource(value = {
            "Valid File Name All Capital Case Hyphenated,IA-0127-HU-02785-2020_2020-07-16-10.07.31.680-UTC_0",
            "Valid File Name All Lower Case Hyphenated,ia-0127-hu-02785-2020_2020-07-16-10.07.31.680-UTC_0",
            "Valid File Name All Capital Case,IA-HU-01234-2018_2020-09-19-10.50.20.150-UTC_0",
            "Valid File Name Case Reference Hyphenated 1,IA-EA-04983-2019-EA-05166-2019_2020-10-01-11.25.25.976-UTC_0",
            "Valid File Name Case Reference Hyphenated 2,IA-HU-15010-2019-HU-15014-2019-HU-15015-2019_2020-10-22-13.43.14.176-UTC_0",
            "Valid File Name Case Reference Hyphenated Lower Case,ia-hu-15010-2019-hu-15014-2019-hu-15015-2019_2020-10-22-13.43.14.176-UTC_0"},
            nullValues = "NIL")
    public void test_positive_case_reference_based_input_for_tribunals(final String inputKey, final String inputValue) {

        Map<String, Object> responseMap = FileNameParser.parseFileName(inputValue);
        switch (inputKey) {
            case "Valid File Name All Capital Case Hyphenated":
                verifyValuesOfMappedResponse(responseMap, "IA", null, "0127-HU-02785-2020",
                        "0", 2020, 7, 16, 10,
                        7, 31, 680000000);
                break;
            case "Valid File Name All Lower Case Hyphenated":
                verifyValuesOfMappedResponse(responseMap, "ia", null, "0127-hu-02785-2020",
                        "0", 2020, 7, 16, 10,
                        7, 31, 680000000);
                break;
            case "Valid File Name All Capital Case":
                verifyValuesOfMappedResponse(responseMap, "IA", null, "HU-01234-2018",
                        "0", 2020, 9, 19, 10,
                        50, 20, 150000000);
                break;
            case "Valid File Name Case Reference Hyphenated 1":
                verifyValuesOfMappedResponse(responseMap, "IA", null, "EA-04983-2019-EA-05166-2019",
                        "0", 2020, 10, 1, 11,
                        25, 25, 976000000);
                break;
            case "Valid File Name Case Reference Hyphenated 2":
                verifyValuesOfMappedResponse(responseMap, "IA", null, "HU-15010-2019-HU-15014-2019-HU-15015-2019",
                        "0", 2020, 10, 22, 13,
                        43, 14, 176000000);
                break;
            case "Valid File Name Case Reference Hyphenated Lower Case":
                verifyValuesOfMappedResponse(responseMap, "ia", null, "hu-15010-2019-hu-15014-2019-hu-15015-2019",
                        "0", 2020, 10, 22, 13,
                        43, 14, 176000000);
                break;
            default:
                throw new IllegalArgumentException("Invalid key request for the Tests ");
        }
    }


    @ParameterizedTest(name = "Positive tests for Royal courts of Justice Location based : {0} --> {1}")
    @CsvSource(value = {
            "Valid File Name All Capital Case Hyphenated,QB-0164-CO-2020-01425_2020-10-23-13.02.08.818-UTC_0",
            "Valid File Name All Lower Case Hyphenated,qb-0164-co-2020-01425_2020-10-23-13.02.08.818-UTC_0"},
            nullValues = "NIL")
    public void test_positive_location_code_based_input_for_royal_courts_of_justice(final String inputKey, final String inputValue) {

        Map<String, Object> responseMap = FileNameParser.parseFileName(inputValue);
        switch (inputKey) {
            case "Valid File Name All Capital Case Hyphenated":
                verifyValuesOfMappedResponse(responseMap, "QB", "164",
                        "CO-2020-01425", "0", 2020, 10, 23,
                        13, 2, 8, 818000000);
                break;
            case "Valid File Name All Lower Case Hyphenated":
                verifyValuesOfMappedResponse(responseMap, "qb", "164",
                        "co-2020-01425", "0", 2020, 10, 23,
                        13, 2, 8, 818000000);
                break;
            default:
                throw new IllegalArgumentException("Invalid key request for the Tests ");
        }
    }

    @ParameterizedTest(name = "Positive tests for Royal courts of Justice non Location based : {0} --> {1}")
    @CsvSource(value = {
            "Valid File Name All Capital Case Hyphenated,QB-CO-2020-01430_2020-09-12-15.00.12.765-UTC_0",
            "Valid File Name No Location Code,QB-QB-2017-002538_2020-10-06-15.20.16.562-UTC_0",
            "Valid File Name With Case Reference Lower Case,QB-QB-blahblahTest_2020-09-22-10.22.53.511-UTC_0",
            "Valid File Name All Capital Case,HF-FD20P00625_2020-10-20-09.28.09.765-UTC_0",
            "Valid File Name Cse Reference Hyphenated,GR-PR-2020-0016-1008_2020-09-21-09.08.26.413-UTC_0"},
            nullValues = "NIL")
    public void test_positive_non_location_code_based_input_for_royal_courts_of_justice(final String inputKey, final String inputvalue) {

        Map<String, Object> responseMap = FileNameParser.parseFileName(inputvalue);
        switch (inputKey) {
            case "Valid File Name All Capital Case Hyphenated":
                verifyValuesOfMappedResponse(responseMap, "QB", null, "CO-2020-01430",
                        "0", 2020, 9, 12, 15,
                        0, 12, 765000000);
                break;
            case "Valid File Name No Location Code":
                verifyValuesOfMappedResponse(responseMap, "QB", null, "QB-2017-002538",
                        "0", 2020, 10, 6, 15,
                        20, 16, 562000000);
                break;
            case "Valid File Name With Case Reference Lower Case":
                verifyValuesOfMappedResponse(responseMap, "QB", null, "QB-blahblahTest",
                        "0", 2020, 9, 22, 10,
                        22, 53, 511000000);
                break;
            case "Valid File Name All Capital Case":
                verifyValuesOfMappedResponse(responseMap, "HF", null, "FD20P00625",
                        "0", 2020, 10, 20, 9,
                        28, 9, 765000000);
                break;
            case "Valid File Name Cse Reference Hyphenated":
                verifyValuesOfMappedResponse(responseMap, "GR", null, "PR-2020-0016-1008",
                        "0", 2020, 9, 21, 9,
                        8, 26, 413000000);
                break;
            default:
                throw new IllegalArgumentException("Invalid key request for the Tests ");
        }

    }

    private void verifyValuesOfMappedResponse(final Map<String, Object> responseMap,
                                              final String jurisdictionCode,
                                              final String locationCode,
                                              final String caseReference,
                                              final String segment,
                                              final int year, final int month,
                                              final int day, final int hour,
                                              final int minute, final int second, final int nano) {
        assertTrue(responseMap.size() == 5 || responseMap.size() == 4);
        assertEquals(jurisdictionCode, responseMap.get("Jurisdiction").toString().trim());
        if (responseMap.size() == 5 && responseMap.get("LocationCode") != null) {
            assertEquals(locationCode, responseMap.get("LocationCode").toString().trim());
        }
        assertEquals(caseReference, responseMap.get("CaseID").toString().trim());
        assertEquals(segment, responseMap.get("Segment").toString().trim());

        LocalDateTime localDateTime = (LocalDateTime) responseMap.get("RecordingDateTime");
        System.out.println("The value of the Returned Date Time" + localDateTime);
        assertEquals(year, localDateTime.getYear());
        assertEquals(month, localDateTime.getMonth().getValue());
        assertEquals(day, localDateTime.getDayOfMonth());
        assertEquals(hour, localDateTime.getHour());
        assertEquals(minute, localDateTime.getMinute());
        assertEquals(second, localDateTime.getSecond());
        assertEquals(nano, localDateTime.getNano());
    }
}