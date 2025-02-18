
import Skeleton  from "@mui/material/Skeleton";
import Alert from "@mui/material/Alert";

export default function Home() {
  return (
    <div>
      {/* For variant="text", adjust the height via font-size */}
      <Skeleton animation="wave" variant="text" sx={{ fontSize: "1rem" }} />

      {/* For other variants, adjust the size with `width` and `height` */}
      <Skeleton animation="wave" variant="circular" width={40} height={40} />
      <Skeleton
        animation="wave"
        variant="rectangular"
        width={210}
        height={60}
      />
      <Skeleton animation="wave" variant="rounded" width={210} height={60} />
      <Alert severity="success">This is a success Alert.</Alert>
      <Alert severity="info">This is an info Alert.</Alert>
      <Alert severity="warning">This is a warning Alert.</Alert>
      <Alert severity="error">This is an error Alert.</Alert>
    </div>
  );
}
