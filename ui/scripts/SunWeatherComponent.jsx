import React, { Component } from 'react';
import axios from 'axios';

class SunWeatherComponent extends Component {
  state = {
    sunrise: null,
    sunset: null,
    temperature: null,
    requests: null,
  }

  componentDidMount = () => {
    axios.get('/data').then((response) => {
      const json = response.data;
      this.setState({
        sunrise: json.sunInfo.sunrise,
        sunset: json.sunInfo.sunset,
        temperature: json.temperature,
        requests: json.requests,
      });
    });
  }

  render = () => {
    return (
      <div>
        <div>Sunrise Time: {this.state.sunrise}</div>
        <div>Sunset Time: {this.state.sunset}</div>
        <div>Current Temperature: {this.state.temperature}</div>
        <div>Requests: {this.state.requests}</div>
      </div>
    );
  }
}

export default SunWeatherComponent;