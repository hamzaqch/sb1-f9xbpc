import { merge } from 'mochawesome-merge';
import { create } from 'mochawesome-report-generator';
import fs from 'fs';

async function generateReport() {
  const reportDir = 'mochawesome-report';
  
  if (!fs.existsSync(reportDir)){
    fs.mkdirSync(reportDir, { recursive: true });
  }

  const jsonReport = await merge({
    files: ['./mochawesome-report/*.json']
  });

  await create(jsonReport, {
    reportDir: reportDir,
    reportTitle: 'Mapbox Test Report',
    reportPageTitle: 'Test Execution Results',
    inline: true,
    charts: true
  });
}

generateReport();