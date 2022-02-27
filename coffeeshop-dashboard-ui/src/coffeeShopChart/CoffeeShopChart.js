import ReactApexCharts from "react-apexcharts";
import React from "react";
import axios from "axios";

export default class CoffeeShopChart extends React.Component {
  interval = null;
  constructor(props) {
    super(props);

    this.fetchAnalytics = this.fetchAnalytics.bind(this);
    this.handleCbuildNodelick = this.buildNode.bind(this);

    this.state = {
      series: [],
      options: {
        chart: {
          height: 350,
          type: "rangeBar",
        },
        plotOptions: {
          bar: {
            horizontal: true,
            barHeight: "50%",
            rangeBarGroupRows: true,
          },
        },
        colors: [
          "#008FFB",
          "#00E396",
          "#FEB019",
          "#FF4560",
          "#775DD0",
          "#3F51B5",
          "#546E7A",
          "#D4526E",
          "#8D5B4C",
          "#F86624",
          "#D7263D",
          "#1B998B",
          "#2E294E",
          "#F46036",
          "#E2C044",
        ],
        fill: {
          type: "solid",
        },
        xaxis: {
          type: "datetime",
        },
        legend: {
          position: "right",
        },
        tooltip: {
          custom: function (opts) {
            return "";
          },
        },
      },
    };
  }

  componentDidMount() {
    this.interval = setInterval(this.fetchAnalytics, 1 * 1000);
    this.fetchAnalytics();
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  fetchAnalytics() {
    axios.post("http://localhost:8080/analytics/data").then((res) => {
      const response = res.data;

      let series = [...response].map((d) => {
        let data = d.data.map((e) => {
          return this.buildNode(e);
        });
        let name = d.name;
        return { data, name };
      });
      this.setState({ series });
    });
  }

  buildNode(e) {
    const x = e.customerId;
    const y = [
      this.buildApexDate(e.startTimestamp),
      this.buildApexDate(e.endTimestamp),
    ];
    return { x, y };
  }

  buildApexDate(timestamp) {
    let date_time = timestamp.split("T");

    let yyyymmddArray = date_time[0].split("-");
    let hh_mm_ssmsszone = date_time[1].split(":");
    let [yyyy, mo, dd] = yyyymmddArray.join(",");
    let [hh, mm] = hh_mm_ssmsszone;
    let [ss, mss] = [
      hh_mm_ssmsszone[2].split(".")[0],
      hh_mm_ssmsszone[2].split(".")[1].split("+")[0],
    ];

    return new Date(yyyy, mo, dd, hh, mm, ss, mss).getTime();
  }

  render() {
    return (
      <div id="chart">
        <ReactApexCharts
          options={this.state.options}
          series={this.state.series}
          type="rangeBar"
          height={350}
        />
      </div>
    );
  }
}
