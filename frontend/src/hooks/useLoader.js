import { useEffect, useState } from 'react';

/**
 * Loads data when the page opens and every time one of the `dependencies` changes.
 * Call `reload()` after saving something to load the data again.
 */
export default function useLoader(load, dependencies = []) {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [version, setVersion] = useState(0);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError('');
    load()
      .then((result) => !cancelled && setData(result))
      .catch((loadError) => !cancelled && setError(loadError.message))
      .finally(() => !cancelled && setLoading(false));
    return () => {
      cancelled = true;
    };
  }, [...dependencies, version]);

  const reload = () => setVersion((current) => current + 1);

  return { data, error, loading, reload };
}
