using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media;

namespace Lab10
{
    public partial class MainWindow : Window
    {
        public class Engine : IComparable
        {
            public string Model { get; set; }
            public double Horsepower { get; set; }
            public double Displacement { get; set; }

            public Engine(double displacement, double horsepower, string model)
            {
                Model = model;
                Horsepower = horsepower;
                Displacement = displacement;
            }
            public Engine() { }

            public int CompareTo(object obj)
            {
                if (obj is Engine otherEngine)
                {
                    return Horsepower.CompareTo(otherEngine.Horsepower);
                }
                throw new ArgumentException("Object is not an Engine");
            }

            public override string ToString()
            {
                return $"{Model},  {Horsepower}, {Displacement}";
            }
        }

        public class Car
        {
            public string Model { get; set; }
            public Engine Motor { get; set; }
            public int Year { get; set; }

            public Car() { }
            public Car(string model, Engine motor, int year)
            {
                Model = model;
                Motor = motor;
                Year = year;
            }
            public override string ToString()
            {
                return $"Model: {Model}, Motor: {Motor}, Year: {Year}";
            }
        }

        public class SearchableAndSortableBindingList : BindingList<Car>
        {
            private bool _bModel = false;
            private bool _bYear = false;
            private bool _bMotor = false;

            public SearchableAndSortableBindingList(List<Car> cars)
            {
                foreach (var car in cars)
                {
                    Add(car);
                }
            }

            public List<Car> Find(string text, string combo)
            {
                var matchingCars = new List<Car>();

                foreach (var car in this)
                {
                    switch (combo)
                    {
                        case "Model":
                            if (car.Model == text)
                            {
                                matchingCars.Add(car);
                            }
                            break;
                        case "Year":
                            if (car.Year == int.Parse(text))
                            {
                                matchingCars.Add(car);
                            }
                            break;
                        case "Motor":
                            if (car.Motor.Model == text)
                            {
                                matchingCars.Add(car);
                            }
                            break;
                    }
                }
                return matchingCars;
            }

            public List<Car> AddElement(string model, string engineModel, double horsepower, double displacement, int year)
            {
                var matchingCars = this.ToList();
                matchingCars.Add(new Car(model, new Engine(displacement, horsepower, engineModel), year));
                return matchingCars;
            }

            public List<Car> Sort(string property)
            {
                var matchingCars = this.ToList();

                switch (property)
                {
                    case "Model":
                        _bModel = !_bModel;
                        return matchingCars = _bModel ? matchingCars.OrderBy(car => car.Model).ToList() :
                            matchingCars.OrderByDescending(car => car.Model).ToList();
                    case "Year":
                        _bYear = !_bYear;
                        return matchingCars = _bYear ? matchingCars.OrderBy(car => car.Year).ToList() :
                            matchingCars.OrderByDescending(car => car.Year).ToList();
                    default:
                        _bMotor = !_bMotor;
                        return matchingCars = _bMotor ? matchingCars.OrderBy(car => car.Motor.Model).ToList() :
                            matchingCars.OrderByDescending(car => car.Motor.Model).ToList();
                }
            }
        }

        private readonly List<Car> _myCars = new List<Car>
        {
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

        private List<Car> _tempCars;
        private BindingList<Car> _myCarsBindingList;
        private SearchableAndSortableBindingList _carList;

        public MainWindow()
        {
            InitializeComponent();
            ComboBox.Items.Add("Model");
            ComboBox.Items.Add("Motor");
            ComboBox.Items.Add("Year");

            _carList = new SearchableAndSortableBindingList(_myCars);
            BindDataToGrid(_myCars);

            QueryExpression();
            MethodBased();
            Task2();
        }

        private void BindDataToGrid(List<Car> cars)
        {
            _myCarsBindingList = new BindingList<Car>(cars);
            CarsDataGrid.ItemsSource = _myCarsBindingList;
        }

        private void Task2()
        {
            Comparison<Car> comparison = (car, b) =>
            {
                if (car.Motor.Horsepower > b.Motor.Horsepower) return 1;
                if (car.Motor.Horsepower < b.Motor.Horsepower) return -1;
                return 0;
            };
            Predicate<Car> predicate = a => a.Motor.Model == "TDI";
            Action<Car> action = a => MessageBox.Show($"2. Model: {a.Model} Silnik: {a.Motor} Rok: {a.Year}");

            _myCars.Sort(comparison);
            _myCars.FindAll(predicate).ForEach(action);
        }

        private void QueryExpression()
        {
            var result = from c in _myCars
                         where c.Model == "A6"
                         let engineType = c.Motor.Model == "TDI" ? "diesel" : "petrol"
                         let hppl = (double)c.Motor.Horsepower / c.Motor.Displacement
                         group hppl by engineType into g
                         orderby g.Average() descending
                         select new
                         {
                             engineType = g.Key,
                             avgHPPL = g.Average()
                         };

            var odp = result.Aggregate("query_expression \n", (current, e) => current + (e.engineType + ": " + e.avgHPPL + " \n"));
            MessageBox.Show(odp);
        }

        private void MethodBased()
        {
            var result = _myCars
                .Where(c => c.Model == "A6")
                .Select(c => new
                {
                    engineType = c.Motor.Model == "TDI" ? "diesel" : "petrol",
                    hppl = (double)c.Motor.Horsepower / c.Motor.Displacement
                })
                .GroupBy(c => c.engineType)
                .Select(g => new
                {
                    engineType = g.Key,
                    avgHPPL = g.Average(c => c.hppl)
                })
                .OrderByDescending(c => c.avgHPPL);

            var odp = result.Aggregate("method-based query \n", (current, e) => current + (e.engineType + ": " + e.avgHPPL + " \n"));
            MessageBox.Show(odp);
        }

        private void HandleKeyPress(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Delete) return;

            _tempCars = _carList.ToList().Where(x => x != (Car)(sender as DataGrid).SelectedItem).ToList();
            _carList = new SearchableAndSortableBindingList(_tempCars);
            BindDataToGrid(_tempCars);
        }

        private void Search_Button(object sender, RoutedEventArgs e)
        {
            var query = SearchTextBox.Text;
            if (ComboBox.SelectedItem is null) return;
            var property = ComboBox.SelectedItem.ToString();

            _tempCars = _carList.Find(query, property);
            BindDataToGrid(_tempCars);
        }

        private void Add_Button(object sender, RoutedEventArgs e)
        {
            var model = Model.Text;
            var engineModel = EngineModel.Text;
            var horsepower = double.Parse(Horsepower.Text); ;
            var displacement = double.Parse(Displacement.Text);
            var year = int.Parse(Year.Text);

            _tempCars = _carList.AddElement(model, engineModel, horsepower, displacement, year);
            _carList = new SearchableAndSortableBindingList(_tempCars);
            BindDataToGrid(_tempCars);
        }

        private void Sort_Model(object sender, RoutedEventArgs e)
        {
            _tempCars = _carList.Sort("Model");
            BindDataToGrid(_tempCars);
        }

        private void Sort_Year(object sender, RoutedEventArgs e)
        {
            _tempCars = _carList.Sort("Year");
            BindDataToGrid(_tempCars);
        }

        private void Sort_Motor(object sender, RoutedEventArgs e)
        {
            _tempCars = _carList.Sort("Motor");
            BindDataToGrid(_tempCars);
        }

        private void Reset_Button(object sender, RoutedEventArgs e)
        {
            BindDataToGrid(_carList.ToList());
        }
    }
}

