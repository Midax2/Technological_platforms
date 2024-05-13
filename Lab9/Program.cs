using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Xml;
using System.Xml.Linq;
using System.Xml.Serialization;
using System.Xml.XPath;

public class Engine
{
    public double Displacement { get; set; }
    public double HorsePower { get; set; }
    public string Type { get; set; }

    public Engine() { }

    public Engine(double displacement, double horsePower, string type)
    {
        Displacement = displacement;
        HorsePower = horsePower;
        Type = type;
    }
}

[XmlType("car")]
public class Car
{
    public string Model { get; set; }

    [XmlElement("engine")]
    public Engine Motor { get; set; }
    public int Year { get; set; }

    public Car() { }

    public Car(string model, Engine motor, int year)
    {
        Model = model;
        Motor = motor;
        Year = year;
    }
}

class Program
{
    static void Main()
    {
        List<Car> myCars = new(){
            new Car("E250", new Engine(1.8, 204, "CGI"), 2009),
            new Car("E350", new Engine(3.5, 292, "CGI"), 2009),
            new Car("A6", new Engine(2.5, 187, "FSI"), 2012),
            new Car("A6", new Engine(2.8, 220, "FSI"), 2012),
            new Car("A6", new Engine(3.0, 295, "TFSI"), 2012),
            new Car("A6", new Engine(2.0, 175, "TDI"), 2011),
            new Car("A6", new Engine(3.0, 309, "TDI"), 2011),
            new Car("S6", new Engine(4.0, 414, "TFSI"), 2012),
            new Car("S8", new Engine(4.0, 513, "TFSI"), 2012)
        };

        // Zapytanie LINQ 1
        var query1 = myCars
            .Where(c => c.Model == "A6")
            .Select(c => new
            {
                engineType = c.Motor.Type == "TDI" ? "diesel" : "petrol",
                hppl = c.Motor.HorsePower / c.Motor.Displacement
            });

        // Zapytanie LINQ 2
        var query2 = query1
            .GroupBy(item => item.engineType)
            .Select(group => new
            {
                EngineType = group.Key,
                AverageHPPL = group.Average(item => item.hppl)
            });

        // Wyświetlanie wyników
        Console.WriteLine("Average HPPL:");
        foreach (var group in query2)
        {
            Console.WriteLine($"{group.EngineType}: {group.AverageHPPL}");
        }

        // Serializacja do XML
        XmlSerializer serializer = new(typeof(List<Car>), new XmlRootAttribute("cars"));
        using (TextWriter writer = new StreamWriter("C:\\Users\\Midax\\Desktop\\Semestr_4_inf\\PT\\Lab_9\\CarsCollection.xml"))
        {
            serializer.Serialize(writer, myCars);
        }

        // Deserializacja z XML
        List<Car> deserializedCars;
        XmlSerializer deserializer = new(typeof(List<Car>), new XmlRootAttribute("cars"));
        using (TextReader reader = new StreamReader("C:\\Users\\Midax\\Desktop\\Semestr_4_inf\\PT\\Lab_9\\CarsCollection.xml"))
        {
            deserializedCars = (List<Car>)deserializer.Deserialize(reader);
        }

        // Wczytanie dokumentu XML
        XElement rootNode = XElement.Load("C:\\Users\\Midax\\Desktop\\Semestr_4_inf\\PT\\Lab_9\\CarsCollection.xml");

        // Obliczanie przeciętnej mocy samochodów o silnikach innych niż TDI
        double avgHP = (double)rootNode.XPathEvaluate("sum(//car[engine/@model != 'TDI']/engine/horsePower) div count(//car[engine/@model != 'TDI'])");

        // Zwracanie modeli samochodów bez powtórzeń
        IEnumerable<XElement> models = rootNode.XPathSelectElements("//car/model[not(. = following::model)]");

        // Zapytanie LINQ do generowania XML
        IEnumerable<XElement> nodes = myCars.Select(car =>
            new XElement("car",
                new XElement("model", car.Model),
                new XElement("engine",
                    new XAttribute("model", car.Motor.Type),
                    new XElement("horsePower", car.Motor.HorsePower),
                    new XElement("displacement", car.Motor.Displacement)
                ),
                new XElement("year", car.Year)
            )
        );

        XElement carsRootNode = new("cars", nodes);
        carsRootNode.Save("C:\\Users\\Midax\\Desktop\\Semestr_4_inf\\PT\\Lab_9\\CarsFromLinq.xml");

        // Generowanie dokumentu XHTML
        XDocument xhtmlDocument = new(
            new XElement("html",
                new XElement("head"),
                new XElement("body",
                    new XElement("table",
                        new XElement("thead",
                            new XElement("tr",
                                new XElement("th", "Model"),
                                new XElement("th", "Engine Type"),
                                new XElement("th", "Horse Power"),
                                new XElement("th", "Displacement"),
                                new XElement("th", "Year")
                            )
                        ),
                        new XElement("tbody",
                            myCars.Select(car =>
                                new XElement("tr",
                                    new XElement("td", car.Model),
                                    new XElement("td", car.Motor.Type),
                                    new XElement("td", car.Motor.HorsePower),
                                    new XElement("td", car.Motor.Displacement),
                                    new XElement("td", car.Year)
                                )
                            )
                        )
                    )
                )
            )
        );

        xhtmlDocument.Save("C:\\Users\\Midax\\Desktop\\Semestr_4_inf\\PT\\Lab_9\\CarsTable.html");

        // Modyfikacja dokumentu XML
        XDocument xmlDoc = XDocument.Load("C:\\Users\\Midax\\Desktop\\Semestr_4_inf\\PT\\Lab_9\\CarsCollection.xml");

        // Zmiana nazwy elementu horsePower na hp
        foreach (var carElement in xmlDoc.Descendants("car"))
        {
            carElement.Element("engine").Element("HorsePower").Name = "hp";
        }

        // Tworzenie atrybutu year w elemencie model
        foreach (var modelElement in xmlDoc.Descendants("Model"))
        {
            modelElement.Add(new XAttribute("year", modelElement.Parent.Element("Year").Value));
            modelElement.Parent.Element("Year").Remove();
        }

        xmlDoc.Save("C:\\Users\\Midax\\Desktop\\Semestr_4_inf\\PT\\Lab_9\\ModifiedCarsCollection.xml");
    }
}
