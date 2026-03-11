import { useState } from 'react';
import type { AxiosResponse } from 'axios';

export function usePdfDownload() {
  const [isDownloading, setIsDownloading] = useState(false);

  async function download(
    fetchFn: () => Promise<AxiosResponse<ArrayBuffer>>,
    filename: string
  ) {
    setIsDownloading(true);
    try {
      const response = await fetchFn();
      const blob = new Blob([response.data], { type: 'application/pdf' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      a.click();
      URL.revokeObjectURL(url);
    } finally {
      setIsDownloading(false);
    }
  }

  return { download, isDownloading };
}
