module.exports = {
  require: ['@babel/register'],
  reporter: 'mochawesome',
  'reporter-option': [
    'reportDir=mochawesome-report',
    'reportFilename=index',
    'html=true',
    'json=true',
    'overwrite=true',
    'timestamp=true',
    'charts=true',
    'code=true',
    'reportTitle=Mapbox Test Report',
    'showPassed=true',
    'showFailed=true',
    'showPending=true',
    'showSkipped=true'
  ]
}