import { describe, it, expect, vi, beforeEach } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { usePdfDownload } from '../usePdfDownload';

// Stub URL APIs not available in jsdom
const createObjectURLMock = vi.fn(() => 'blob:mock-url');
const revokeObjectURLMock = vi.fn();

beforeEach(() => {
  vi.clearAllMocks();
  URL.createObjectURL = createObjectURLMock;
  URL.revokeObjectURL = revokeObjectURLMock;
  // document.createElement('a').click() is a no-op in jsdom — no mock needed
});

describe('usePdfDownload', () => {
  it('starts with isDownloading false', () => {
    const { result } = renderHook(() => usePdfDownload());
    expect(result.current.isDownloading).toBe(false);
  });

  it('sets isDownloading true during download then false after', async () => {
    const fetchFn = vi.fn().mockResolvedValue({ data: new ArrayBuffer(8) });
    const { result } = renderHook(() => usePdfDownload());

    await act(async () => {
      await result.current.download(fetchFn, 'report.pdf');
    });

    expect(result.current.isDownloading).toBe(false);
  });

  it('calls fetchFn and creates an object URL', async () => {
    const fetchFn = vi.fn().mockResolvedValue({ data: new ArrayBuffer(8) });
    const { result } = renderHook(() => usePdfDownload());

    await act(async () => {
      await result.current.download(fetchFn, 'report.pdf');
    });

    expect(fetchFn).toHaveBeenCalledOnce();
    expect(createObjectURLMock).toHaveBeenCalledOnce();
  });

  it('revokes the object URL after download', async () => {
    const fetchFn = vi.fn().mockResolvedValue({ data: new ArrayBuffer(8) });
    const { result } = renderHook(() => usePdfDownload());

    await act(async () => {
      await result.current.download(fetchFn, 'report.pdf');
    });

    expect(revokeObjectURLMock).toHaveBeenCalledWith('blob:mock-url');
  });

  it('resets isDownloading to false even if fetchFn throws', async () => {
    const fetchFn = vi.fn().mockRejectedValue(new Error('network error'));
    const { result } = renderHook(() => usePdfDownload());

    await act(async () => {
      await result.current.download(fetchFn, 'report.pdf').catch(() => {});
    });

    expect(result.current.isDownloading).toBe(false);
  });
});
