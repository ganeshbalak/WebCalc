package com.everyone.counts.sample;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.everyone.counts.sample.data.TestData;

public class InterviewTest
{
    static String driverPath = "D:\\Users\\gbalakrishnan\\workspace\\EveryoneCounts\\src\\test\\resources\\";
    public WebDriver driver;
    private String baseUrl;
    private String csvFile = "Input.csv";

    @BeforeClass
    public void setUp()
    {
        System.out.println("launching chrome browser");
        System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
        System.setProperty("os.name", "windows");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        baseUrl = "http://web2.0calc.com";

    }

    @Test(dataProvider = "provideData")
    public void readCSVCalculate(TestData testData) throws InterruptedException
    {
        char[] firstInputCharacters = testData.getFirstInput().toCharArray();
        char[] secondInputCharacters = testData.getSecondInput().toCharArray();

        driver.get(baseUrl + "/");
        driver.findElement(By.id("BtnClear")).click();

        for (char inputCharacter : firstInputCharacters)
        {
            driver.findElement(By.id(getId(inputCharacter))).click();
        }

        getOperationButtonId(testData);

        for (char inputCharacter : secondInputCharacters)
        {
            driver.findElement(By.id(getId(inputCharacter))).click();
        }

        driver.findElement(By.id("BtnCalc")).click();
        Thread.sleep(2000);

        assertEquals(driver.findElement(By.id("input")).getAttribute("value"), testData.getResult());
    }

    private void getOperationButtonId(TestData testData)
    {
        if (testData.getOperation().equalsIgnoreCase("Multiplication"))
        {
            driver.findElement(By.id("BtnMult")).click();
        }
        else if (testData.getOperation().equalsIgnoreCase("Division"))
        {
            driver.findElement(By.id("BtnDiv")).click();
        }
        else if (testData.getOperation().equalsIgnoreCase("Addition"))
        {
            driver.findElement(By.id("BtnPlus")).click();
        }
        else if (testData.getOperation().equalsIgnoreCase("Subtraction"))
        {
            driver.findElement(By.id("BtnMinus")).click();
        }
    }

    private String getId(char inputCharacter)
    {
        switch (inputCharacter)
        {
        case '0':
        {
            return "Btn0";
        }
        case '1':
        {
            return "Btn1";
        }
        case '2':
        {
            return "Btn2";
        }
        case '3':
        {
            return "Btn3";
        }
        case '4':
        {
            return "Btn4";
        }
        case '5':
        {
            return "Btn5";
        }
        case '6':
        {
            return "Btn6";
        }
        case '7':
        {
            return "Btn7";
        }
        case '8':
        {
            return "Btn8";
        }
        case '9':
        {
            return "Btn9";
        }
        case '-':
        {
            return "BtnMinus";
        }
        default:
        {
            new RuntimeException("Invalid character: " + inputCharacter);
        }
        }

        return null;
    }

    @DataProvider(name = "provideData")
    public Object[][] provideData()
    {
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        List<TestData> testDataList = new ArrayList<TestData>();
        Object[][] objects = null;

        try
        {
            br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + csvFile)));

            while ((line = br.readLine()) != null)
            {
                String[] data = line.split(csvSplitBy);
                TestData testData = new TestData();
                testData.setFirstInput(data[0]);
                testData.setSecondInput(data[1]);
                testData.setOperation(data[2]);
                testData.setResult(data[3]);
                testDataList.add(testData);
            }

            objects = new Object[testDataList.size()][1];

            for (int i = 0; i < testDataList.size(); i++)
            {
                objects[i][0] = testDataList.get(i);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return objects;

    }

    @AfterClass
    public void tearDown()
    {
        if (driver != null)
        {
            System.out.println("Closing chrome browser");
            driver.quit();
        }
    }

}
